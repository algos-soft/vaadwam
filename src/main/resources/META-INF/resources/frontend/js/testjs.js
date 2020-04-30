var scrollablediv;
var scrollposdiv;
var bwide_js;
var bnarrow_js;
var btall_js;
var bshort_js;

/**
 * Inizializzazione dello script.
 */
function initTestJS() {
    let template=document.getElementById("template");
    let container=template.shadowRoot.getElementById("container");
    scrollablediv=getElementInContainer(container,"scrollablediv")
    scrollposdiv=getElementInContainer(container,"scrollposdiv")

    let buttondivjs=getElementInContainer(container,"buttondivjs")
    bwide_js=getElementInContainer(buttondivjs,"bwide_js")
    bnarrow_js=getElementInContainer(buttondivjs,"bnarrow_js")
    btall_js=getElementInContainer(buttondivjs,"btall_js")
    bshort_js=getElementInContainer(buttondivjs,"bshort_js")

    // add scroll listener to the scrollablediv
    scrollablediv.addEventListener("scroll", containerScrolled)
    containerScrolled() // first update

}


function wider(){
    changeWidth(10);
}

function narrower(){
    changeWidth(-10);
}

function taller(){
    changeHeight(10);
}

function shorter(){
    changeHeight(-10);
}

function changeWidth(diff){
    let w = scrollablediv.offsetWidth+diff;
    scrollablediv.style.width=w+"px";
    console.log( "width="+w+"px" );
}

function changeHeight(diff){
    let h = scrollablediv.offsetHeight+diff;
    scrollablediv.style.height=h+"px";
    console.log( "height="+h+"px" );
}



function scrollUp(){
    scrollTo(scrollablediv.scrollLeft, scrollablediv.scrollTop-10);
}

function scrollDn(){
    scrollTo(scrollablediv.scrollLeft, scrollablediv.scrollTop+10);
}

function scrollLt(){
    scrollTo(scrollablediv.scrollLeft-10, scrollablediv.scrollTop);
}

function scrollRt(){
    scrollTo(scrollablediv.scrollLeft+10, scrollablediv.scrollTop);
}




function getElementInContainer(container, childID) {
    let elm = {};
    let elms = container.getElementsByTagName("*");
    for (let i = 0; i < elms.length; i++) {
        if (elms[i].id === childID) {
            elm = elms[i];
            break;
        }
    }
    return elm;
}

function sum(n1, n2){
    return n1+n2;
}

function javaSum(){
    server.sum(randomInt(10,100), randomInt(10,100));
}

function showResult(n1, n2, tot){
    let s = n1+"+"+n2+" (JS) = "+tot+" (Java)";
    alert(s);
}

function randomInt(min, max) {
    return Math.random() * (max - min) + min;
}

function containerScrolled(){
    scrollposdiv.textContent="scroll pos: x="+scrollablediv.scrollLeft+", y="+scrollablediv.scrollTop;
    //server.tabScrolled(container.scrollLeft, container.scrollTop);
}

// scrolla il div alla posizione specificata
function scrollTo(x, y){
    console.log( "scrollTo invoked: "+x+", "+y);
    scrollablediv.scrollTo(x,y);
}



