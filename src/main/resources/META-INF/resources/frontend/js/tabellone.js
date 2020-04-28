/**
* Responsabilità di questa funzione è di individuare e ritornare l'elemento del DOM
* che contiene il tabellone e che quindi gestisce le barre di scorrimento per il
* controllo dell'offset.
* Se la struttura che ospita il tabellone cambia questo metodo va adeguato.
*/
function findContentElement() {
    let appLayout=document.getElementsByTagName("vaadin-app-layout")[0];
    let drawer = appLayout.shadowRoot.getElementById("drawer");
    let content = drawer.nextElementSibling;
    return content
}

// scrolla il tabellone alla posizione specificata
function scrollTo(x, y){
    console.log( "scrollContent invoked: "+x+", "+y);
    let content = findContentElement();
    content.scrollTo(x,y);
}

// ritorna la posizione corrente di scrolling del tabellone
function getScrollPosition(){
}
