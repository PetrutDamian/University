var lista = undefined;
var count = undefined;
var actual = 0;
var items = undefined;
function initialize(){
    lista = document.getElementById("list");
    count = lista.childElementCount;
    document.getElementById("counter").innerText ="1 out of "+count;
    items = lista.children;
    items[0].style.opacity = "100%";
    for(let i=1;i<items.length;i++) {
        items[i].style.display = "none";
        items[i].style.opacity = "0%";
    }
    setInterval(next,4000);
}
function previous(){
    if(actual>0){
        actual -=1;
        var previous = undefined;
        previous = actual+1;
        document.getElementById("counter").innerText = (actual+1)+" out of "+count;
        setTimeout(function(){
            items[previous].style.opacity = 0;
            items[actual].style.opacity = 0.2;
        },1);
        setTimeout(function(){
            items[previous].style.display = "none";
            items[actual].style.display = "block";
        },400);
        setTimeout(function(){
            items[actual].style.opacity = 1;
        },460);
    }

}
function next(){
    if (actual<count-1){
        actual +=1;
        var previous = undefined;
        previous = actual-1;
        document.getElementById("counter").innerText = (actual+1)+" out of "+count;
        setTimeout(function(){
            items[previous].style.position="absolute";
            items[previous].style.opacity=0;
            items[actual].style.display = "block";
        },1);
        setTimeout(function(){
            items[actual].style.opacity = 1;
        },350);
        setTimeout(function(){
            items[previous].style.position="static";
            items[previous].style.display = "none";
        },1000);
    }
}