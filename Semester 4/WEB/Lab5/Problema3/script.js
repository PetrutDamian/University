var identity = ['1','1','2','2','3','3','4','4','5','5','6','6','7','7','8','8'];
var flipped = []
var discovered = []
function shuffle(array){
    var size = array.length;
    var aux = Number()
    for (var i = size-1; i>0;i--){
        r = Math.floor(Math.random() * (i+1));
        aux = array[r];
        array[r] = array[i];
        array[i] = aux;
    }
}
function reveal(element){

    if (flipped.length <2) {
        if (element.attr("valid") == "da") {
            element.attr("valid", "nu");
            var span = element.children("span");
            if (flipped.length<2) {
                flipped.push(span);
                span.css("visibility", "visible");
            }
            if (flipped.length == 2) {
                if (flipped[0].text() == flipped[1].text()) {
                    discovered.push(flipped[0]);
                    discovered.push(flipped[1]);
                    flipped = []
                    if (discovered.length == 16) {
                        flipped = [1, 2, 3];
                        alert("Ati castigat");
                    }
                } else {
                    var s1 = flipped[0];
                    var s2 =flipped[1];
                    flipped = [1,2,3];
                    function hideBack() {
                        s1.css("visibility", "hidden");
                        s1.parent().attr("valid", "da");
                        s2.css("visibility", "hidden");
                        s2.parent().attr("valid", "da");
                        flipped = []
                    }

                    setTimeout(hideBack, 500);
                }
            }
        }
    }
}
$(function(){
    shuffle(identity)
    var str = ""
    for(var i=0;i<identity.length;i++)
        $("#container").append("<div><span>"+identity[i]+"</span></div>");
    $("#container div").click(function(){
        reveal($(this));
    });
    $("#container div").attr("valid","da");
    function hide() {
        $("div span").css("visibility","hidden");
    }
    setTimeout(hide,400);
});

