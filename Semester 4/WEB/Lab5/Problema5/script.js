var $lista = undefined;
var actual = 0;
var $items = undefined;
function showCounter(){
    $("#counter").text((actual+1)+"/"+items.length);
}
$(function(){
    items = $("li");
    items.css("display","none");
    $(items[0]).css("display","block");
    $("#prev").click(previous);
    $("#next").click(next);
    showCounter();
        setInterval(next,3500);
});

function previous(){
    let aux = actual;
    $("button").prop("disabled",true);
    setTimeout(function(){
        $(items[aux]).fadeOut(500);
    },10);

    actual -= 1;
    if(actual<0)
        actual = items.length-1;
    setTimeout(function () {
        $(items[actual]).fadeIn(500,function () {
            $("button").prop("disabled",false);
        });
    },10);
    showCounter();
}
function next(){
    let aux = actual;
    $("button").prop("disabled",true);
    setTimeout(function(){
        $(items[aux]).fadeOut(500);
    },10);

    actual += 1;
    if(actual==items.length)
        actual = 0;
    setTimeout(function () {
        $(items[actual]).fadeIn(500,function () {
            $("button").prop("disabled",false);
        });
    },10);
    showCounter();
}