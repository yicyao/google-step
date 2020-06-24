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



// Navigate to next slide
function plusSlides(slideNum) {
  showSlides("arrow", slideIndex += slideNum);

}

// Navigate to slide indicated by slideNum
function currentSlide(slideNum) {

  showSlides("dots", slideIndex = slideNum);
}

// Show only the current slide and dot needed, hide others

function showSlides(type, slideNum) {
  let slides;
  if (type === "dots") { 
    slides = document.getElementsByClassName("miniSlides");
  }
  else {
    slides = document.getElementsByClassName("mySlides");
  }

  const dots = document.getElementsByClassName("dot");


  //wraps slide number around to insure in bounds
  if (slideNum > slides.length) {slideIndex = 1}
  if (slideNum < 1) {slideIndex = slides.length}

  for (let i = 0; i < dots.length; i++) {
    dots[i].className = dots[i].className.replace(" active", "");
  }

  dots[slideIndex-1].className += " active";

  for (let i = 0; i < slides.length; i++) {
    slides[i].style.display = "none";
  }
  
  //shows only current slide
  slides[slideIndex-1].style.display = "block";
}