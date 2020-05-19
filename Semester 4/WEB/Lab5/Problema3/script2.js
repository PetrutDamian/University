var identity = ['1','1','2','2','3','3','4','4','5','5','6','6','7','7','8','8'];
var flipped = []
var discovered = []

function shuffle(array){
    var size = array.length
    var aux = Number()
    for (var i = size-1; i>0;i--){
        r = Math.floor(Math.random() * (i+1));
        aux = array[r];
        array[r] = array[i];
        array[i] = aux;
    }
}
function reveal(element){
    if(flipped.length<=2) {
        if (element.attr("valid") == "da") {
            element.attr("valid", "nu");
            var span = element.children("span");
            var index = parseInt($("span").index(span));
            if (flipped.length < 2) {
                flipped.push(span);
                $("#container :nth-child(" + (index + 1) + ")").css("background", "url('sprite2.png')" + span.text() * 130 * -1 + "px 0");
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
                    var p1 = flipped[0].parent();
                    var p2 = flipped[1].parent();
                    flipped = [1, 2, 3];

                    function hideBack() {
                        p1.css("background", "none").attr("valid", "da");
                        p2.css("background", "none").attr("valid", "da");
                        flipped = []
                        //$("div :nth-child("+flipped[0].text+")").css("background","none");
                        //$("div :nth-child("+flipped[1].text+")").css("background","none");

                    }

                    setTimeout(hideBack, 300);
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
    $("#container div").attr("valid","da");
    $("#container div").click(function(){
        reveal($(this));
    });
    for (var i = 0; i < 16; i++) {
        var j = i+1;
        var q=  $("div :nth-child("+j+")");
        $("#container :nth-child("+j+")").css("background", "url('sprite2.png')" + (identity[i] * 130 * -1) + "px 0");
    }
    $("span").css("visibility","hidden");
    function hideImages(){
        $("#container div").css("background","none");
    }
    setTimeout(hideImages,400);
});

