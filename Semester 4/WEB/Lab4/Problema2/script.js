function validate(){
    errors = "";
    nume = document.getElementById("nume");
    if (nume.value == "") {
        errors += "Nu ati completat numele!\n";
        nume.style.outline="red 2px solid";
    }
    else{
        if(!(/^[a-z A-Z]+$/.test(nume.value)))
        {
            errors += "Numele nu e valid!\n";
            nume.style.outline="red 2px solid";
        }
        else
            nume.style.outline="none";
    }

    varsta = document.getElementById("varsta");
    if (varsta.value == "") {
        errors += "Nu ati completat varsta!\n";
        varsta.style.outline="red 2px solid";
    }
    else{
        if (varsta.value<=0)
        {
            errors += "Varsta trebuie sa fie pozitiva!\n";
            varsta.style.outline="red 2px solid";
        }
        else
            varsta.style.outline="none";
    }
    email = document.getElementById("email");
    if (email.value == ""){
        errors += "Nu ati completat adresa de email!\n";
        email.style.outline="red 2px solid";
    }
    else{
        if(!(/^.+@.+\..+$/.test(email.value))){
            errors += "Adresa de email invalida!\n";
            email.style.outline="red 2px solid";
        }
        else
            email.style.outline="none";
    }
    dataNasterii = document.getElementById("dataNasterii");
    if(dataNasterii.value == ""){
        errors += "Nu ati ales data nasterii!\n";
        dataNasterii.style.outline = "red 2px solid"
    }
    else
        dataNasterii.style.outline = "none";
    if (errors == ""){
        alert("Toate campurile sunt completate corect");
        return true;
    }
    else{
        alert(errors);
        return false;
    }
}