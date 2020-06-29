// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/* Creates Slideshow Carousel to go through my projects*/

/**
 * @param slideNum: Index of Slide to navigate to
 */

// Navigates to next slide
function plusSlides(slideNum) {
  showSlides("arrow", (slideIndex += slideNum));

  // depends on which set of slides
  firstSlides = document
    .getElementById("slide1")
    .getElementsByClassName("miniSlides");
  showMiniSlides(firstSlides);

  secondSlides = document
    .getElementById("slide2")
    .getElementsByClassName("miniSlides");
  showMiniSlides(secondSlides);
}

// Navigates to slide indicated by slideNum
function currentSlide(slideNum) {
  showSlides("dots", (slideIndex = slideNum));
}

// Shows slides within slides
function showMiniSlides(slides) {
  for (let i = 0; i < slides.length; i++) {
    slides[i].style.display = "none";
  }
  slides[0].style.display = "block";
}

// Shows only the current slide and dot needed, hide others
function showSlides(type, slideNum) {
  let slides;
  const dots = document.getElementsByClassName("dot");

  if (type === "dots") {
    slides = document.getElementsByClassName("miniSlides");
  } else {
    slides = document.getElementsByClassName("mySlides");
  }

  //wraps slide number around to insure in bounds
  if (slideNum > slides.length) {
    slideIndex = 1;
  }
  if (slideNum < 1) {
    slideIndex = slides.length;
  }

  //hides all dots and slides
  for (let i = 0; i < dots.length; i++) {
    dots[i].className = dots[i].className.replace(" active", "");
  }

  for (let i = 0; i < slides.length; i++) {
    slides[i].style.display = "none";
  }

  //shows only current slide and dot
  slides[slideIndex - 1].style.display = "block";
  dots[slideIndex - 1].className += " active";
}

//says hello
async function sayHello() {
  const response = await fetch('/data');
  console.log('getting data')
  const hello = await response.text();

  document.getElementById('hello-container').innerHTML = hello;
}