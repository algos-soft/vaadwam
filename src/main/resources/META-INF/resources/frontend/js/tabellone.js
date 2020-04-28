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
    container = findContainerElement();
    container.addEventListener("scroll", containerScrolled)
    console.log( "scroll listener added");
}

function containerScrolled(){
    console.log( "container scrolled: x="+container.scrollLeft+", y="+container.scrollTop);
    server.tabScrolled(container.scrollLeft, container.scrollTop);
}

// scrolla il tabellone alla posizione specificata
function scrollTo(x, y){
    console.log( "scrollContent invoked: "+x+", "+y);
    container.scrollTo(x,y);
}

