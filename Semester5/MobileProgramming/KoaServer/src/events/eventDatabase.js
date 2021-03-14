import dataStore from "nedb-promise";

export class EventDatabase {
  constructor({ filename, autoload }) {
    this.store = dataStore({ filename, autoload });
    this.pageSize = 12;
  }

  async findPage(props, pageNumber) {
    const elems = await this.store.find(props);
    const pageElems = elems.slice(
      this.pageSize * (pageNumber - 1),
      this.pageSize * pageNumber
    );
    return pageElems;
  }
  async find(props) {
    return this.store.find(props);
  }

  async findOne(props) {
    return this.store.findOne(props);
  }

  async insert(event) {
    return this.store.insert(event);
  }

  async update(props, event) {
    return this.store.update(props, event);
  }

  async remove(props) {
    return this.store.remove(props);
  }
}

export var eventDB = new EventDatabase({
  filename: "./db/events.json",
  autoload: true,
});
