class GameOver extends Phaser.Scene {
  constructor() {
    super("gameover");
    this.score = "";
    this.winner = "";
  }
  create() {
    this.add.image(0, 0, "tiles").setOrigin(0, 0);
    this.add.text(50, 50, this.score, {
      fontSize: "26px",
      fill: "black",
    });
    this.add.text(150, 50, this.winner + " wins!", {
      fontSize: "26px",
      fill: "black",
    });
  }
  init(data) {
    console.log(data);
    this.score = data.score;
    this.winner = data.winner;
  }
}
