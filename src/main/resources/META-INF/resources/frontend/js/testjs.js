var scrollablediv;
var bwide;
var bnarrow;
var btall;
var bshort;

/**
 * Inizializzazione dello script.
 */
function initTestJS() {
    let template=document.getElementById("template");
    let container=template.shadowRoot.getElementById("container");
    scrollablediv=getElementInContainer(container,"scrollablediv")
    let buttondiv=getElementInContainer(container,"buttondiv")
    bwide=getElementInContainer(buttondiv,"bwide")
    bnarrow=getElementInContainer(buttondiv,"bnarrow")
    btall=getElementInContainer(buttondiv,"btall")
    bshort=getElementInContainer(buttondiv,"bshort")
}


function wider(){
    changeWidth(10);
}

function narrower(){
    changeWidth(-10);
}

function higher(){
    changeHeight(10);
}

function shorter(){
    changeHeight(-10);
}



function changeWidth(diff){
    let w = scrollablediv.offsetWidth+diff;
    scrollablediv.style.width=w+"px";
}

function changeHeight(diff){
    let h = scrollablediv.offsetHeight+diff;
    scrollablediv.style.height=h+"px";
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

