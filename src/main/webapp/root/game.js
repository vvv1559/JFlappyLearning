/**
 * Player class
 */
class Player {
    /**
     * Produce new Player instance
     */
    constructor() {
        this.populationSize = 10;
        this.worldHeight = 0;
        this.apiPath = '/play';

        this._httpGet('/reset');

        // let Neuvol = new Neuroevolution({
        //   population: 50,
        //   network: [2, [2], 1],
        // });
    }

    _httpGet(path, parameters) {
        const params = parameters
            ? ('?' + Object.keys(parameters).map(key => `${key}=${parameters[key]}`).join("&"))
            : '';

        const xmlHttp = new XMLHttpRequest();
        xmlHttp.open("GET", `${this.apiPath}${path}${params}`, false);
        xmlHttp.send(null);
        return xmlHttp.responseText;
    };

    _httpPost(path, body) {
        const xmlHttp = new XMLHttpRequest();
        xmlHttp.open("POST", `${this.apiPath}${path}`, false);
        xmlHttp.send(body);
        return xmlHttp.responseText;
    };

    /**
     * Fix bird score before it died
     * @param {Bird} bird
     * @param {number} score
     */
    birdDied(bird, score) {
        this._httpGet('/died', {index: bird.index, score: score});
    }

    /**
     * Process new round and return birds count for next round
     * @return {number}
     */
    nextRoundBirdsCount() {
        this.populationSize = Number.parseInt(this._httpGet('/nextGen'));
        return this.populationSize;
    }


    /**
     * Answer the question: need bird's jump or not
     * @param {Bird[]} birds
     * @param {PipesLineWithHole[]} pipeLines List of pipe lines in the game
     * @return {boolean}
     */
    needFlap(birds, pipeLines) {
        //                let inputs = [this.birds[i].top / this.height, nextHole];

        let nextHoleTop = 0;
        const birdsNextPipeIndex = pipeLines.findIndex(pipeLine => pipeLine.left > birds[0].left);
        if (birdsNextPipeIndex !== -1) {
            nextHoleTop = pipeLines[birdsNextPipeIndex].holeTop;
        }

        const request = {};
        birds.forEach(b => request[b.index] = [b.top / this.worldHeight, nextHoleTop]);

        return JSON.parse(this._httpPost('/needFlap', JSON.stringify(request)));
        // return Math.round(Math.random() * 14 - 13) > 0;

        // if (res > 0.5) {
        //   this.birds[i].flap();
        // }
    }
}

// socket.onmessage = function (event) {
//     console.log('Received ' + event.data);
// };
//
// socket.onerror = function (error) {
//     console.log('Error ' + error.message);
// };
//
// setInterval(function () {
//     socket.send('Hello, server');
//
//     const buffer = new ArrayBuffer(8);
//     const dv = new DataView(buffer, 0);
//     dv.setInt32(0, 7);
//     dv.setFloat32(4, 3.14);
//     socket.send(dv.buffer)
// }, 1000);

{
    const timeouts = [];
    const zeroTimeoutMessage = 'zero-timeout-message';

    const handleMessage = (event) => {
        if (event.source === window && event.data === zeroTimeoutMessage) {
            event.stopPropagation();
            timeouts.shift()();
        }
    };

    window.addEventListener('message', handleMessage, true);

    window.setZeroTimeout = (fn) => {
        timeouts.push(fn);
        window.postMessage(zeroTimeoutMessage, '*');
    };
}

let FPS = 60;

const speed = (speedMul) => {
    FPS = speedMul * 60;
};

/**
 * Format seconds into HH:mm:ss
 * @param {number} seconds
 * @return {string} formatted time
 */
const toHHMMSS = (seconds) => {
    let minutes = Math.floor(seconds / 60);
    seconds -= minutes * 60;

    let hours = Math.floor(minutes / 60);
    minutes -= hours * 60;

    const fillLeadZero = val => (val < 10) ? "0" + val : val;

    return [hours, minutes, seconds].map(fillLeadZero).join(":");
};

window.onload = () => {
    const sprites = {
        bird: './img/bird.png',
        background: './img/background.png',
        pipetop: './img/pipetop.png',
        pipebottom: './img/pipebottom.png'
    };

    let inProcess = 0;
    let images = {};

    const startGame = (player) => {
        const game = new Game(document.querySelector('#flappy'), images);
        game.start();
        game.update();
        game.display();
    };

    for (const name in sprites) {
        inProcess++;
        const imagePath = sprites[name];

        images[name] = new Image();
        images[name].src = imagePath;
        images[name].onload = () => {
            if (--inProcess === 0) {
                let player = new Player('ws://localhost:8081/play/');
                setTimeout(() => startGame(player), 500);
            }
        }
    }
};

class Bird {
    constructor(index) {
        this.left = 80;
        this.top = 250;
        this.width = 40;
        this.height = 30;

        this.alive = true;
        this.gravity = 0;
        this.velocity = 0.3;
        this.jump = -6;

        this.index = index;
    }

    flap() {
        this.gravity = this.jump;
    };

    update() {
        this.gravity += this.velocity;
        this.top += this.gravity;
    };

    get right() {
        return this.left + this.width;
    }

    get bottom() {
        return this.top + this.height;
    }

    /**
     * Check that bird does in game area and does not contact any pipe
     * @param {number} maxHeight
     * @param {PipesLineWithHole[]} pipeLines
     */
    checkAlive(maxHeight, pipeLines) {
        if (this.bottom <= 0 || this.top >= maxHeight) {
            this.alive = false;
            return;
        }

        for (let i in pipeLines) {
            let pipeLine = pipeLines[i];
            if (this.right < pipeLine.left || this.left > pipeLine.right) {
                continue;
            }

            if (this.top < pipeLine.holeTop || this.bottom > pipeLine.holeBottom) {
                this.alive = false;
                return;
            }
        }
    }

}

class PipesLineWithHole {

    constructor(x, holeTop, holeHeight, holeWidth) {
        this.left = x;
        this.holeTop = holeTop;
        this.holeHeight = holeHeight;
        this.width = holeWidth;
    }

    update(deltaX) {
        this.left -= deltaX;
    };

    isOut() {
        return this.right < 0;
    }

    get right() {
        return this.left + this.width;
    }

    get holeBottom() {
        return this.holeTop + this.holeHeight;
    }
}

class Game {
    /**
     *
     * @param {HTMLCanvasElement} canvas
     * @param {Object.<string, Image>} images
     * @param {Player} player
     */
    constructor(canvas, images) {
        this.ctx = canvas.getContext('2d');
        this.worldWidth = canvas.width;
        this.worldHeight = canvas.height;

        this.images = images;

        this.backgroundSpeed = 0.5;
        this.backgroundX = 0;

        this.pipeWidth = 50;
        this.pipeSpeed = 3;

        this.pipesSpawnInterval = 90 * this.pipeSpeed;

        this.player = new Player();
        this.player.worldHeight = this.worldHeight;
        this.maxScore = 0;
        this.generation = 0;

        this.playedSeconds = 0;
    }

    start() {
        this.generation++;
        this.score = 0;
        this.pipeLines = [];
        this.birds = [];

        const nextRoundBirdsCount = this.player.nextRoundBirdsCount();
        for (let i = 0; i < nextRoundBirdsCount; i++) {
            this.birds.push(new Bird(i))
        }
    }


    update() {
        this.playedSeconds++;
        this.backgroundX += this.backgroundSpeed;

        const pipeLines = this.pipeLines;

        const flapInfo = this.player.needFlap(this.birds, pipeLines);
        for (let i = 0; i < this.birds.length; i++) {
            const bird = this.birds[i];

            if (flapInfo[bird.index] > 0) {
                bird.flap();
            }

            bird.update();
            bird.checkAlive(this.worldHeight, pipeLines);

            if (!bird.alive) {
                this.player.birdDied(bird, this.score);
                this.birds.splice(i, 1);

                if (!this.birds.length) {
                    this.start();
                }
            }
        }

        for (let i = 0; i < pipeLines.length; i++) {
            const pipeLine = pipeLines[i];
            pipeLine.update(this.pipeSpeed);

            if (i === 0 && pipeLine.isOut()) {
                pipeLines.shift();
            }
        }

        const lastPipeLine = pipeLines[pipeLines.length - 1];
        if (!pipeLines.length || this.worldWidth - lastPipeLine.left >= this.pipesSpawnInterval) {
            const minPipeHeight = 50, holeHeight = 120;
            const holeTop = Math.round(Math.random() * (this.worldHeight - 2 * minPipeHeight - holeHeight)) + minPipeHeight;

            pipeLines.push(new PipesLineWithHole(this.worldWidth, holeTop, holeHeight, this.pipeWidth));
        }

        this.maxScore = Math.max(++this.score, this.maxScore);
        let self = this;

        if (FPS === 0) {
            setZeroTimeout(() => self.update());
        } else {
            setTimeout(() => self.update(), 1000 / FPS);
        }
    }

    display() {
        this.ctx.clearRect(0, 0, this.worldWidth, this.worldHeight);
        const images = this.images;
        const backgroundSprite = images['background'];

        const backgroundRepeats = Math.ceil(this.worldWidth / backgroundSprite.width) + 1;
        const backgroundShift = Math.floor(this.backgroundX % backgroundSprite.width);
        for (let i = 0; i < backgroundRepeats; i++) {
            this.ctx.drawImage(backgroundSprite, i * backgroundSprite.width - backgroundShift, 0)
        }

        for (let i in this.pipeLines) {
            const pipeLine = this.pipeLines[i];
            const topPipeSprite = images['pipetop'];
            const bottomPipeSprite = images['pipebottom'];

            this.ctx.drawImage(topPipeSprite, pipeLine.left, pipeLine.holeTop - topPipeSprite.height, pipeLine.width, topPipeSprite.height);
            this.ctx.drawImage(bottomPipeSprite, pipeLine.left, pipeLine.holeBottom, pipeLine.width, bottomPipeSprite.height);
        }

        this.ctx.fillStyle = '#FFC600';
        this.ctx.strokeStyle = '#CE9E00';
        for (let i in this.birds) {
            this.ctx.save();
            const bird = this.birds[i];
            //Set center or rotation. Now start of bird image at [-bird.width / 2; -bird.height / 2]
            this.ctx.translate(bird.left + bird.width / 2, bird.top + bird.height / 2);
            this.ctx.rotate(Math.PI / 2 * bird.gravity / 20);
            this.ctx.drawImage(images['bird'], -bird.width / 2, -bird.height / 2, bird.width, bird.height);
            this.ctx.restore();
        }

        this.ctx.fillStyle = 'white';
        this.ctx.font = '20px Oswald, sans-serif';
        this.ctx.fillText('Score : ' + this.score, 10, 25);
        this.ctx.fillText('Max Score : ' + this.maxScore, 10, 50);
        this.ctx.fillText('Generation : ' + this.generation, 10, 75);
        this.ctx.fillText('Alive : ' + this.birds.length + ' / ' + this.player.populationSize, 10, 100);
        this.ctx.fillText('Play time : ' + toHHMMSS(this.playedSeconds), 10, 125);

        let self = this;
        requestAnimationFrame(() => self.display());
    }
}
