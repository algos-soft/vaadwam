// riferimento al container del tabellone
var container;

/**
* Responsabilità di questa funzione è di individuare e ritornare l'elemento del DOM
* che contiene il tabellone e che quindi gestisce le barre di scorrimento per il
* controllo dell'offset.
* Se la struttura che ospita il tabellone cambia questo metodo va adeguato.
*/
function findContainerElement() {
    let appLayout=document.getElementsByTagName("vaadin-app-layout")[0];
    let drawer = appLayout.shadowRoot.getElementById("drawer");
    let elem = drawer.nextElementSibling;
    return elem
}

function setupScrollListener(){

    var runOnScroll =  function(evt) {
        console.log(evt.target);
    };

    var tabellone = document.querySelector("#tabellonegrid");
    tabellone.addEventListener("scroll", runOnScroll, {passive: true});
    console.log( "scroll listener added");
}

function containerScrolled(){
    console.log( "container scrolled: x="+container.scrollLeft+", y="+container.scrollTop);
    server.tabScrolled(container.scrollLeft, container.scrollTop);
}

// scrolla il tabellone alla posizione specificata
function scrollTabelloneTo(x, y){
    console.log( "scrollTo invoked: "+x+", "+y);
    container.scrollTo(x,y);
}


document.addEventListener('DOMContentLoaded', (event) => {
    console.log('DOM fully loaded and parsed');

    var runOnScroll =  function(evt) {
        console.log(evt.target);
    };

    var tabellone = document.querySelector("#tabellonegrid");
    tabellone.addEventListener("scroll", runOnScroll, {passive: true});

});

