odr.ready(function() {
    // SVG on the page should only be manipulated after the page is
    // finished loading
    var buttons = document.getElementsByTagName('button');
    for (var i = 0; i < buttons.length; i++) {
        buttons[i].disabled = false;
    }
});

function changeColors() {
    // get elements from our embedded SVG first

    // use getElementById
    var circle = document.getElementById('myCircle');

    // change using setAttribute
    circle.setAttribute('stroke', 'green');

    // can also use style property
    circle.style.fill = '#8A2BE2';

    // change the value inside our SVG OBJECT now

    // use the 'contentDocument' property to navigate into the SVG OBJECT
//    var doc = document.getElementById('mySVGObject').contentDocument;
//    circle = doc.getElementById('myCircle');
//    circle.style.fill = '#8A2BE2';
}

function changeText() {
    // use getElementsByTagNameNS to get our text from our embedded SVG

    // 'svgns' is a 'magic' variable that we make available; it is just
    // the SVG namespace 'http://www.w3.org/2000/svg' so you don't always
    // have to remember it.  We also make the variable 'xlinkns' available.
    var textElems = document.getElementsByTagNameNS(svgns, 'text');

    // change the text Hello World to Goodbye World
    for (var i = 0; i < textElems.length; i++) {
        if (textElems[i].childNodes[0].nodeValue == 'Hello World') {
            textElems[i].childNodes[0].nodeValue = 'Goodbye World';
        }
    }

    // change the text inside our SVG OBJECT as well
//    var doc = document.getElementById('mySVGObject').contentDocument;
//    textElems = doc.getElementsByTagNameNS(svgns, 'text');
//    for (var i = 0; i < textElems.length; i++) {
//        if (textElems[i].childNodes[0].nodeValue == 'Hello World') {
//            textElems[i].childNodes[0].nodeValue = 'Goodbye World';
//        }
//    }
}

$(function() {
    $( ".node" ).resizable(odr.settings.resizing.jQueryUiSettings);
    $( ".node" ).draggable(odr.settings.dragging.jQueryUiSettings);
    $( ".associationHelper" ).draggable({containment : "parent", cursorAt : {top : 5, left : 5}});


    $(".hide").click(function() {
        var elements = document.getElementsByClassNS(svgns, "someCircleClass", "circle");

        for(var i = 0; i < elements.length; i++) {
            elements[i].style.display = "none";
        }
    });


    var shape = new odr.Shape();

//    shape.bind("positionChanged", function(target) {
//        console.log(this);
//        console.log(target);
//    }.createDelegate({foo : "bla"}));

    var rectangle = {
        id : "SomeVeryFancyId",
        listener : function(thingThatChanged) {
            console.log(this.id);
            console.log(thingThatChanged);
        }
    }

    shape.bind(odr.Shape.listener.positionChanged, rectangle.listener.createDelegate(rectangle));

    shape.x();
});
