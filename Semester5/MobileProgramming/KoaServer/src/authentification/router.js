import Router from "koa-router";
import jwt from "jsonwebtoken";
import { jwtConfig } from "../utils";
import { userDB } from "./userDatabase";

const createToken = (user) => {
  return jwt.sign(
    { username: user.username, _id: user._id },
    jwtConfig.secret,
    {
      expiresIn: 60 * 60 * 2,
    }
  );
};

export const router = new Router();

router.post("/login", async (ctx) => {
  const credentials = ctx.request.body;
  const response = ctx.response;

  console.log("here");
  const user = await userDB.findOne({ username: credentials.username });
  console.log(ctx.request.body);
  if (user && credentials.password === user.password) {
    response.status = 201; // created
    response.body = { token: createToken(user) };
  } else {
    response.status = 400; // bad request
    response.body = { issue: [{ error: "Invalid credentials" }] };
  }
});
