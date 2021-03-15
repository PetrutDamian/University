class Preload extends Phaser.Scene {
  constructor() {
    super("preload");
    console.log("preload");
  }

  preload() {
    this.load.image("tiles", "assets/gray.png");
    this.load.image("ground", "assets/platform.png");
    this.load.image("star", "assets/star.png");
    this.load.image("bomb", "assets/bomb.png");
    this.load.image("tank", "assets/tankmic.png");
    this.load.image("wall", "assets/wall.png");
    this.load.image("tank2", "assets/tank2mic.png");
    this.load.image("ball", "assets/dot.png");
    this.load.spritesheet("boom", "assets/boomSmall.png", {
      frameHeight: 89.5,
      frameWidth: 102,
    });
    console.log("preload loaded");
  }
  create() {
    console.log("preload create");
    this.scene.start("game");
  }
}
