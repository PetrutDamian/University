import Router from "koa-router";
import { broadcast } from "../utils";
import { eventDB } from "./eventDatabase";

export const router = new Router();

router.get("/", async (ctx) => {
  const response = ctx.response;
  const userId = ctx.state.user._id;
  response.body = await eventDB.find({ userId });
  response.status = 200;
});
router.get("/page/:nr", async (ctx) => {
  const userId = ctx.state.user._id;
  console.log("userID : " + userId);
  ctx.response.body = await eventDB.findPage({ userId }, ctx.params.nr);
  ctx.response.status = 200;
});

router.del("/:id", async (ctx) => {
  const userId = ctx.state.user._id;
  const event = await eventDB.findOne({ _id: ctx.params.id });
  if (!event || userId !== event.userId) ctx.response.status = 404;
  else {
    await eventDB.remove({ _id: ctx.params.id });
    ctx.response.status = 200;
    ctx.response.body = ctx.params.id;
    broadcast(userId, {
      type: "deleted",
      payload: { _id: ctx.params.id },
    });
  }
});

router.put("/:id", async (ctx) => {
  console.log("Entered PUT...........");
  const tag = Number(ctx.request.header.etag);
  const event = ctx.request.body;
  console.log("Here is event: " + JSON.stringify(event));
  const pathId = ctx.params.id;
  const eventId = event._id;
  var response = ctx.response;
  if (eventId && eventId !== pathId) {
    response.body = { message: "Param id and body _id should be the same" };
    response.status = 400; // bad request
    return;
  } else {
    console.log("finding one ...");
    var eventDb = await eventDB.findOne({ _id: eventId });
    console.log("found one : " + JSON.stringify(eventDb));
    if (!eventDb) response.status = 404;
    else {
      console.log("This is tag: ");
      console.log(tag);
      console.log(eventDb.version);
      console.log(tag === eventDb.version);
      if (isNaN(tag) || tag === eventDb.version) {
        console.log("entered here");
        const count = await eventDB.update(
          { _id: eventId },
          { ...event, userId: ctx.state.user._id, version: eventDb.version + 1 }
        );
        if (count != 1) response.status = 405;
        else {
          response.body = eventDb;
          response.status = 200;
          broadcast(ctx.state.user._id, {
            type: "updated",
            payload: { ...event, version: eventDb.version + 1 },
          });
        }
      } else {
        console.log("entered here");
        broadcast(ctx.state.user._id, {
          type: "conflict",
          payload: eventDb,
        });
        response.status = 409;
        response.body = "Update conflict!";
      }
    }
  }
});

router.post("/", async (ctx) => {
  const event = { ...ctx.request.body, version: 1 };
  const createdEvent = await eventDB.insert({
    ...event,
    userId: ctx.state.user._id,
    _id: undefined,
  });
  ctx.response.status = 201;
  ctx.response.body = createdEvent;

  broadcast(ctx.state.user._id, {
    type: "created",
    payload: { ...event, _id: createdEvent._id },
  });
});
