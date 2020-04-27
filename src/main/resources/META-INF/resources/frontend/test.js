
//$( window ).on( "load", function() {
//    console.log( "window loaded" );
//});



document.addEventListener("load", function(event) {
    //console.log('DOM is loaded');
    console.log('page is fully loaded');
    scrollContent();
});


function getScrollPosition(){

}

function scrollContent(){
    let content = findContentElement();
    content.scrollTo(0,200);
}

function findContentElement() {
    let appLayout=document.getElementsByTagName("vaadin-app-layout")[0];
    let drawer = appLayout.shadowRoot.getElementById("drawer");
    let content = drawer.nextElementSibling;
    return content
}


