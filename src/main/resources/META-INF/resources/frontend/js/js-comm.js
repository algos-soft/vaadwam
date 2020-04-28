/*!
 * Basic Java/JS communications script.
 *
 * Ensures that the Java class calls JS functions only when the page is really completed
 * and visible to the user.
 *
 *
 * In the Java class:
 *
 * Include in the constructor:
 *
 *    UI.getCurrent().getPage().executeJs("registerServer($0)", getElement());
 *
 * Define the following method, and it will be called when the page is really available
 *
 *   @ClientCallable
 *   public void pageReady(){
 *       getElement().executeJs("scrollContent()");
 *   }
 */

// called from the Java class (server) to register a reference
// to the server itself in order to enable JavaScript
// to call methods on the server.
function registerServer(serverElement){
    server=serverElement.$server;
    console.log( "Java server registered" );
    server.pageReady();
}

function callServer(){
    server.greet("ciao from client");
    console.log( "server method called" );
}




