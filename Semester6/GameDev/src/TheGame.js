class TheGame extends Phaser.Scene {
  constructor() {
    super("game");
    console.log("TheGame");
    this.walls = null;
    this.cursors = null;
    this.player = null;
    this.player2 = null;
    this.keys = null;
    this.bullets = null;
    this.locked = false;
    this.locked2 = false;
    this.score1 = 0;
    this.score2 = 0;
    this.scoreText1 = "0";
    this.scoreText2 = "0";
    this.over;
  }
  create() {
    this.over = this.scene.start;
    this.add.image(0, 0, "tiles").setOrigin(0, 0);
    this.anims.create({
      key: "boom",
      frames: this.anims.generateFrameNumbers("boom", {
        start: 0,
        end: 11,
      }),
      frameRate: 25,
      hideOnComplete: true,
    });
    this.scoreText1 = this.add.text(20, 16, "score:0", {
      fontSize: "26px",
      fill: "green",
    });
    this.scoreText2 = this.add.text(1350, 16, "score:0", {
      fontSize: "26px",
      fill: "red",
    });
    this.walls = this.physics.add.staticGroup();
    for (let i = 0; i <= 700; i += 33) {
      this.walls.create(0, i, "wall");
      this.walls.create(1500, i, "wall");
    }
    for (let i = 0; i <= 1500; i += 33) {
      this.walls.create(i, 0, "wall");
      this.walls.create(i, 700, "wall");
    }
    for (let i = 500; i <= 700; i += 33) this.walls.create(300, i, "wall");
    for (let i = 0; i <= 200; i += 33) this.walls.create(1200, i, "wall");
    for (let i = 500; i <= 1000; i++) this.walls.create(i, 400, "wall");
    for (let i = 500; i <= 1000; i++) this.walls.create(i, 200, "wall");

    this.player = this.physics.add.sprite(150, 600, "tank");
    this.player2 = this.physics.add.sprite(1350, 100, "tank2");
    this.cursors = this.input.keyboard.createCursorKeys();
    this.keys = {
      w: this.input.keyboard.addKey("W"),
      space: this.input.keyboard.addKey("SPACE"),
      shift: this.input.keyboard.addKey("SHIFT"),
      a: this.input.keyboard.addKey("A"),
      s: this.input.keyboard.addKey("S"),
      d: this.input.keyboard.addKey("D"),
    };

    this.bullets = this.physics.add.group();
    this.physics.add.collider(this.player, this.walls);
    this.physics.add.collider(this.player2, this.walls);
    this.physics.add.collider(this.bullets, this.walls);
    this.physics.add.collider(
      this.player,
      this.bullets,
      this.hitBullet,
      null,
      this
    );
    this.physics.add.collider(
      this.player2,
      this.bullets,
      this.hitBullet2,
      null,
      this
    );
  }
  hitBullet(player, bullet) {
    bullet.destroy();
    let boom = this.physics.add.sprite(bullet.x, bullet.y, "boom");
    boom.anims.play("boom");
    this.score2++;
    this.scoreText2.setText("score:" + this.score2);
    if (this.score2 === 3)
      setTimeout(() => {
        this.gameoverScene.call(this);
      }, 300);
  }
  hitBullet2(player2, bullet) {
    bullet.destroy();
    let boom = this.physics.add.sprite(bullet.x, bullet.y, "boom");
    boom.anims.play("boom");
    this.score1++;
    this.scoreText1.setText("score:" + this.score1);
    if (this.score1 === 3) {
      setTimeout(() => {
        this.gameoverScene.call(this);
      }, 300);
    }
  }
  gameoverScene() {
    this.scene.start("gameover", {
      score: this.score1 + " : " + this.score2,
      winner: this.score1 > this.score2 ? "Green player" : "Red player",
    });
  }

  getXY(angle) {
    if (angle >= 0 && angle < 90) {
      return { x: angle, y: -90 + angle };
    } else if (angle >= 90 && angle < 180) {
      return { x: 180 - angle, y: angle - 90 };
    } else if (angle < 0 && angle >= -90) {
      return { x: angle, y: -90 - angle };
    } else {
      return { x: -180 - angle, y: -90 - angle };
    }
  }
  getTanDeg(deg) {
    var rad = (deg * Math.PI) / 180;
    return Math.tan(rad);
  }

  checkKeys(p, right, left, up, down) {
    if (right.isDown) {
      p.angle += 1;
    }
    if (left.isDown) p.angle -= 1;
    if (up.isDown) {
      const { x, y } = this.getXY(p.angle);
      p.setVelocityX(x);
      p.setVelocityY(y);
    } else if (down.isDown) {
      const { x, y } = this.getXY(p.angle);
      p.setVelocityX(-1 * x);
      p.setVelocityY(-1 * y);
    } else {
      p.setVelocityX(0);
      p.setVelocityY(0);
    }
  }
  getBallXY(angle, x, y) {
    if (angle >= 0 && angle < 90) {
      const tangent = this.getTanDeg(90 - angle);
      const x2 = Math.sqrt(1600 / (1 + tangent * tangent));
      const y2 = Math.sqrt(1600 - x2 * x2);
      return { x: x + x2, y: y - y2 };
    } else if (angle < 0 && angle >= -90) {
      const tangent = this.getTanDeg(90 - angle);
      const x2 = Math.sqrt(1600 / (1 + tangent * tangent));
      const y2 = Math.sqrt(1600 - x2 * x2);
      return { x: x - x2, y: y - y2 };
    } else if (angle >= 90 && angle < 180) {
      const tangent = this.getTanDeg(450 - angle);
      const x2 = Math.sqrt(1600 / (1 + tangent * tangent));
      const y2 = Math.sqrt(1600 - x2 * x2);
      return { x: x + x2, y: y + y2 };
    } else {
      const tangent = this.getTanDeg(90 - angle);
      const x2 = Math.sqrt(1600 / (1 + tangent * tangent));
      const y2 = Math.sqrt(1600 - x2 * x2);
      return { x: x - x2, y: y + y2 };
    }
  }
  checkFire(p, key) {
    if (key.isDown) {
      let isLocked;
      if (p === this.player) isLocked = this.locked;
      else isLocked = this.locked2;
      if (!isLocked) {
        const { x, y } = this.getBallXY(p.angle, p.x, p.y);
        const trajectory = this.getXY(p.angle);
        let ball = this.bullets.create(x, y, "ball");
        ball.body.setVelocityX(trajectory.x * 3);
        ball.body.setVelocityY(trajectory.y * 3);
        ball.body.setBounce(1, 1);
        setTimeout(() => {
          ball.destroy();
        }, 15000);
        if (p === this.player) {
          this.locked = true;
          setTimeout(() => {
            this.locked = false;
          }, 5000);
        } else {
          this.locked2 = true;
          setTimeout(() => {
            this.locked2 = false;
          }, 5000);
        }
      }
    }
  }
  update() {
    this.checkKeys(
      this.player,
      this.cursors.right,
      this.cursors.left,
      this.cursors.up,
      this.cursors.down
    );
    this.checkKeys(
      this.player2,
      this.keys.d,
      this.keys.a,
      this.keys.w,
      this.keys.s
    );
    this.checkFire(this.player, this.keys.space);
    this.checkFire(this.player2, this.keys.shift);
  }
}
