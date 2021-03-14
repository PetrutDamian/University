import dataStore from "nedb-promise";

export class UserDatabase {
  constructor({ filename, autoload }) {
    this.store = dataStore({ filename, autoload });
  }

  async findOne(props) {
    return this.store.findOne(props);
  }

  async insert(user) {
    return this.store.insert(user);
  }
}

export var userDB = new UserDatabase({
  filename: "./db/users.json",
  autoload: true,
});
