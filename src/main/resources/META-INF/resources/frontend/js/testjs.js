var scrollablediv;
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

    let buttondivjs=getElementInContainer(container,"buttondivjs")
    bwide_js=getElementInContainer(buttondivjs,"bwide_js")
    bnarrow_js=getElementInContainer(buttondivjs,"bnarrow_js")
    btall_js=getElementInContainer(buttondivjs,"btall_js")
    bshort_js=getElementInContainer(buttondivjs,"bshort_js")

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

