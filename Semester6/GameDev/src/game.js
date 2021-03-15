var config = {
  type: Phaser.AUTO,
  width: 1500,
  height: 700,
  physics: {
    default: "arcade",
    arcade: {
      debug: false,
    },
  },
  scene: [Preload, TheGame, GameOver],
};

var game = new Phaser.Game(config);
