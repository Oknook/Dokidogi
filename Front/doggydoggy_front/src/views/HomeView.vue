<template>
    <main>   
      <header>
        <h1>
          <span class="span1">Doki</span><br />
          <span class="span2">Dogi</span><br />
          <span class="span3">Service</span>
        </h1>
        <address>2024 SSAFY 2nd semester<br /> First PROJECT</address>
      </header>
  
      <!-- section -->
      <setion class="banner">
        <video
          class="banner_video"
          src="../assets/video/video01.mp4"
          muted
          loop
          autoplay
        ></video>
      </setion>   
      
      <section>
        <article
          id="article_video01"
          class="article_video"
          v-on:mouseenter="run"
          v-on:mouseleave="pause"
          v-on:click="aside_on"
        >
          <div class="inner">
            <div class="txt">
              <h2>01</h2>
              <p>Euichan<br /><br />Back</p>
              <figure>
                <video
                  class="article_figure_video"
                  src="../assets/video/20240213_104611.mp4"
                  muted
                  loop
                ></video>
              </figure>
            </div>
          </div>
        </article>
  
        <article
          id="article_video02"
          class="article_video"
          v-on:mouseenter="run"
          v-on:mouseleave="pause"
          v-on:click="aside_on"
        >
          <div class="inner">
            <div class="txt">
              <h2>02</h2>
              <p>DongHo<br /><br />GOD</p>
              <figure>
                <video
                  class="article_figure_video"
                  src="../assets/video/20240212_200234.mp4"
                  muted
                  loop
                ></video>
              </figure>
            </div>
          </div>
        </article>
  
        <article
          id="article_video03"
          class="article_video"
          v-on:mouseenter="run"
          v-on:mouseleave="pause"
          v-on:click="aside_on"
        >
          <div class="inner">
            <div class="txt">
              <h2>03</h2>
              <p>Chiseung<br /><br />webRTC</p>
              <figure>
                <video
                  class="article_figure_video"
                  src="../assets/video/20240213_104601.mp4"
                  muted
                  loop
                ></video>
              </figure>
            </div>
          </div>
        </article>
  
        <article
          id="article_video04"
          class="article_video"
          v-on:mouseenter="run"
          v-on:mouseleave="pause"
          v-on:click="aside_on"
        >
          <div class="inner">
            <div class="txt">
              <h2>04</h2>
              <p>JunHyoung<br /><br />Infra</p>
              <figure>
                <video
                  class="article_figure_video"
                  src="../assets/video/20240213_104641.mp4"
                  muted
                  loop
                ></video>
              </figure>
            </div>
          </div>
        </article>
      </section>
  
      <!-- aside -->
      <aside id="aside_video01">
        <div class="txt">
          <h1>01</h1>
          <p>Healing`<br /><br />Movie01</p>
          <span id="close_text" v-on:click="close">CLOSE</span>
        </div>
        <figure id="figure_text">
          <video
            id="aside_video1"
            src="../assets/video/video01.mp4"
            muted
            loop
          ></video>
        </figure>
      </aside>    
    </main>
  </template>
  
  <script>
  export default {
    // name: 'Home',
    data() {
      return {}
    },
    created() {
      document.title = this.$route.name
    },
    methods: {
      run: function (e) {
        e.currentTarget.querySelector('video').play()
      },
      pause: function (e) {
        e.currentTarget.querySelector('video').pause()
      },
      aside_on: function (e) {
        const aside = document.querySelector('aside')
        const tit = e.currentTarget.querySelector('h2').innerText
        const txt = e.currentTarget.querySelector('p').innerText
        const vidSrc = e.currentTarget.querySelector('video').getAttribute('src')
  
        aside.querySelector('h1').innerText = tit
        aside.querySelector('p').innerText = txt
        aside.querySelector('video').setAttribute('src', vidSrc)
  
        aside.querySelector('video').play()
        aside.classList.add('on')
        window.scrollTo(0, 0)
      },
      close: function () {
        const aside = document.querySelector('aside')
        aside.classList.remove('on')
        aside.querySelector('video').pause()
      }
    },
    components: {
    }
  }
  </script>
  
  <style>
  @charset "utf-8";
  @import url('https://fonts.googleapis.com/css2?family=Alegreya+Sans+SC:w-ght@100&display=swap'); 
  
  * {
    margin: 0px;
    padding: 0px;
    box-sizing: border-box;
  }
  
  ul,
  ol,
  li {
    list-style: none;
  }
  
  a {
    text-decoration: none;
    outline: 0;
  }
  main {
    width: 100%;
    height: 100%;
    background: #ffffff;
  }
  /* 애니메이션 */
  @keyframes moveIn {
    from {
      position: absolute;
      right: -100%;
      width: 65%;
      margin-top: 6%;
      opacity: 0;
    }
    to {
      position: absolute;
      right: 5%;
      width: 65%;
      margin-top: 6%;
      opacity: 1;
    }
  }
  @keyframes moveIn2 {
    from {
      position: absolute;
      opacity: 0;
      left: -10%;
    }
    to {
      position: absolute;
      opacity: 1;
      left: 35%;
    }
  }
  @keyframes moveIn3 {
    from {
      position: absolute;
      /* left: -10%; */
      opacity: 0;
      margin-top: 110%;
    }
    to {
      position: absolute;
      /* left: 33%; */
      opacity: 1;
      margin-top: 87%;
    }
  }
  @keyframes noneShow {
    from {
      opacity: 0;
    }
    to {
      opacity: 0;
    }
  }
  
  /* header */
  main header {
    width: 20%;
    height: 100%;
    padding: 5rem 6vw 1vh 5vw;
    display: flex;
    flex-wrap: wrap;
    align-content: space-between;
    justify-content: center;
    position: relative;
    z-index: 3;
    float: left;
    /*    background: #333;*/
    /* border:10px solid pink; */
  }
  main header h1 {
    /*안쪽의 자간 줄높이 초기화*/
    font-size: 0px;
    font-weight: 700;
    margin-bottom: 50px;
    color: green;
  }
  main header h1 .span1 {
    animation: moveIn2 3s ease;
  }
  main header h1 .span2 {
    animation: moveIn2 2s ease;
  }
  main header h1 .span3 {
    animation: moveIn2 1s ease;
  }
  main header h1::after {
    content: '';
    display: block;
    width: 12vw;
    height: 2px;
    background: #777;
    margin-top: 20px;
    transition: 1.5s;
  }
  
  main header h1 span {
    font: normal 3.8vw/2 'arial';
  }
  main header address {
    margin-top: 10%;
    color: #4d4b4b;
    animation: noneShow 3s;
    animation: moveIn3 4s ease;
    font: normal 1.1vw/1.5 'arial';
  }
  
  
  /* banner */
  main .banner {
    width: 65%;
    float: left;
  }
  main .banner .banner_video {
    width: 100%;
    object-fit: cover;
    filter: saturate(50%);  
    margin-top: 10%;
    animation: moveIn 3s ease;
  }
  
  
  /* section */
  main section {
    position: relative;
    width: 90%;
    height: 100%;
    display: flex;
    padding-top: 70px;
    z-index: 1;
  
    /* border:10px solid yellow; */
  }
  main section article {
    position: relative;
    flex: 1;
    padding: 50px 40px 100px;
    opacity: 0.6;
    transition: flex 1s, opacity 1s;
    z-index: 1;
    cursor: pointer;
  }
  main section article::before {
    content: '';
    display: block;
    width: 80%;
    height: 95%;
    background: linear-gradient(180deg, rgba(246,245,233,1) 0%, rgba(242,245,183,1) 26%, rgba(251,255,172,1) 100%);
    
    border-radius: 5px;
    position: absolute;
    top: 0px;
    left: -200px;
    opacity: 0;
    transition: 1s;
  }
  main section article .inner {
    width: 100%;
    height: 100%;
    display: flex;
    flex-wrap: wrap;
    align-content: space-between;
    position: relative;
  }
  main section article .inner .txt {
    width: 100%;
    transition: 0.5s;
  }
  main section article .inner .txt h2 {
    font: normal 2.5vw/1 'Alegreya Sans Sc';
    color: #fff;
    margin-bottom: 40px;
    padding-top: 70px;
    transition: 1s;
  }
  main section article .inner .txt h2::after {
    content: '';
    display: block;
    width: 40px;
    height: 2px;
    background: #777;
    margin-top: 20px;
    transition: 1.5s;
  }
  main section article .inner .txt p {
    font: 14px/1.4 'arial';
    font-weight: 600;
    color: #000000;
  }
  main section article .inner figure {
    width: 100%;
    height: 15vh;
    background: #777;
    transform-origin: left center;
    box-shadow: 5px 5px 30px rgba(0, 0, 0, 0.3);
    position: relative;
    overflow: hidden;
    filter: saturate(10%);
    transition: 1s;
  }
  main section article .inner figure video {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transform: scale(1.1);
  }
  
  /* article영역에 호버 효과 */
  main section article:hover {
    flex: 1.5;
    opacity: 1;
    z-index: 2;
  }
  main section article:hover::before {
    left: 0px;
    opacity: 1;
  }
  main section article:hover .inner .txt {
    width: 75%;
  }
  main section article:hover .inner .txt h2 {
    font-size: 5vw;
    padding-top: 0px;
  }
  main section article:hover .inner .txt h2::after {
    width: 120px;
    background: rgb(242, 242, 242);
  }
  main section article:hover .inner figure {
    height: 25vh;
    filter: saturate(120%);
    transform: translateY(3vh);
  }
  
  /* aside */
  main aside {
    width: 100%;
    height: 100vh;
    background: #222;
    position: absolute;
    top: 0px;
    left: -100%;
    opacity: 0; /*처음에 숨기기*/
    z-index: 2;
    box-sizing: border-box;
    padding: 10vh 7vw 14vh 10vw;
    display: flex;
    justify-content: space-between;
    transition: 1s;
  }
  main aside.on {
    opacity: 1;
    left: 0%;
    transition: 2s;
  }
  main aside .txt {
    width: 25%;
    height: 100%;
    position: relative;
    left: 10%;
  }
  main aside .txt h1 {
    font: normal 6vw/1 'Alegreya Sans Sc';
    color: #ddd;
    margin-bottom: 20px;
  }
  main aside .txt p {
    font: 3.1vw/0.7 'arial';
  
    color: #666;
    margin-bottom: 60px;
  }
  main aside .txt span {
    font-size: 30px;
    cursor: pointer;
    color: white;
    margin-left: 10%;
  }
  main aside .txt span:hover {
    color: orange;
  }
  main aside figure {
    width: 72%;
    height: 100%;
    position: relative;
    left: 4%;
  }
  main aside figure video {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
  </style>
  