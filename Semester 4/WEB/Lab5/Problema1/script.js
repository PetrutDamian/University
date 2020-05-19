
function move(source, destination){
    $(source).find(":selected").appendTo(destination)
}
$(function(){
    $("#1").dblclick(function(){
        move("#1","#2");
    })
    $("#2").dblclick(function(){
        move("#2","#1");
    })
})