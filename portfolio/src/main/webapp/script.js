// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the 'License');
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an 'AS IS' BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/* Creates Slideshow Carousel to go through my projects*/

// Navigates to next slide
function plusSlides(slideNum) {
  showSlides('arrow', (slideIndex += slideNum));

  // depends on which set of slides
  firstSlides =
      document.getElementById('slide1').getElementsByClassName('miniSlides');
  showMiniSlides(firstSlides);

  secondSlides =
      document.getElementById('slide2').getElementsByClassName('miniSlides');
  showMiniSlides(secondSlides);
}

// Navigates to slide indicated by slideNum
function currentSlide(slideNum) {
  showSlides('dots', (slideIndex = slideNum));
}

// Shows slides within slides
function showMiniSlides(slides) {
  for (let i = 0; i < slides.length; i++) {
    slides[i].style.display = 'none';
  }
  slides[0].style.display = 'block';
}

// Shows only the current slide and dot needed, hide others
function showSlides(type, slideNum) {
  let slides;
  const dots = document.getElementsByClassName('dot');

  if (type === 'dots') {
    slides = document.getElementsByClassName('miniSlides');
  } else {
    slides = document.getElementsByClassName('mySlides');
  }

  // Wraps slide number around to insure in bounds
  if (slideNum > slides.length) {
    slideIndex = 1;
  }
  if (slideNum < 1) {
    slideIndex = slides.length;
  }

  // Hides all dots and slides
  for (let i = 0; i < dots.length; i++) {
    dots[i].className = dots[i].className.replace(' active', '');
  }

  for (let i = 0; i < slides.length; i++) {
    slides[i].style.display = 'none';
  }

  // Shows only current slide and dot
  slides[slideIndex - 1].style.display = 'block';
  dots[slideIndex - 1].className += ' active';
}

// Fetches comments from /data and displays
async function getComment() {
  fetch('/data').then((response) => response.json()).then((greetings) => {
    const greetingList = document.getElementById('comment-container');
    greetingList.innerHTML = '';
    for (const message of greetings) {
      greetingList.appendChild(createTableElement(message));
    }
  });
}

// Creates an table element for comments containing text with name and date
function createTableElement(text) {
  const trElement = document.createElement('tr');
  const tdElementName = document.createElement('td');
  tdElementName.innerHTML = `${text.text} - ${text.name} on 
    ${new Date(text.timestamp).toDateString()}`;
  trElement.appendChild(tdElementName);
  return trElement;
}

// Creates a map and adds it to the page
function createMap() {
  const map = new google.maps.Map(
      document.getElementById('map'),
      {center: {lat: 37.422, lng: -122.084}, zoom: 16});
  const geocoder = new google.maps.Geocoder();
  document.getElementById('submit').addEventListener('click', function() {
    geocodeAddress(geocoder, map);
  });
}

// Geocodes map to location indicated by user
function geocodeAddress(geocoder, resultsMap) {
  const address = document.getElementById('address').value;
  geocoder.geocode({address: address}, function(results, status) {
    if (status === 'OK') {
      resultsMap.setCenter(results[0].geometry.location);
      const marker = new google.maps.Marker({
        map: resultsMap,
        position: results[0].geometry.location,
      });
    } else {
      alert('Geocode was not successful for the following reason: ' + status);
    }
  });
}

// Fills out form using suggested locations
$(document).ready(function() {
  $('#address_return a').click(function() {
    const value = $(this).html();
    const input = $('#address');
    input.val(value);
  });
});