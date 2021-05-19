let last_known_scroll_position = 0;
let ticking = false;

function doSomething( scroll_pos ) {
    let topHeader = document.querySelector( ".topHeader" );
    if( scroll_pos > 0 ) {
        topHeader.classList.add( "d-none" );
    } else {
        topHeader.classList.remove( "d-none" );
    }
}

window.addEventListener( 'scroll', function( e ) {
  last_known_scroll_position = window.scrollY;

  if ( !ticking ) {
    window.requestAnimationFrame( function() {
      doSomething( last_known_scroll_position );
      ticking = false;
    } );

    ticking = true;
  }
});