// let seach_box_text_list = ["butter", "chocolate", "curd", "egg", "chips", "panner", "milk", "rice", "sugar", "bread"];
// let length = seach_box_text_list.length();

let b = document.getElementById("searchboxid");
b.addEventListener("click", () => {
    let main_content = document.getElementById("main_id");
    let address_box = document.getElementById("address");
    let log_id = document.getElementById("log-id");
    let searchbox = document.getElementById("searchboxid");
    main_content.hidden = "true";
    address_box.style.display = "none";
    log_id.style.display = "none";
    searchbox.style.width = "73vw";
    searchbox.style.marginLeft = "-0.5vw";
    searchbox.value = "Search for atta dal and more";
    searchbox.style.fontSize = "2vh";
    searchbox.style.color = "rgb(83, 83, 83)";
    // searchbox.style.border = "border: 1px solid rgba(0, 0, 0, 0.04)";
    searchbox.style.border = "border: 1px solid red";

}, false);

let numberOfItemsinCart = 0;
let totalRupeeincart = 0;

function addtocart(event, i) {
    let mycartimage2 = document.getElementById("cart-id-2");
    mycartimage2.style.display = "none";
    ++numberOfItemsinCart;
    let mainparent = event.target.parentNode.parentNode.parentNode;
    let c = mainparent.childNodes[1];
    let originalRNodes = c.children[4];
    let toarb = document.getElementsByClassName("to-add-remove")[i];
    let toclosed = document.getElementsByClassName("add-button-class")[i];
    let writtedintext = document.getElementsByClassName("current-quantity")[i];
    toclosed.style.display = "none";
    toarb.style.display = "block";
    let oldr = document.getElementsByClassName("old-ruppee")[i];
    oldr.style.top = "-9.3vh";
    writtedintext.textContent = 1;
    writtedintext.style.display = "inline-block";
    let d = originalRNodes.textContent.split(" ");
    let rSign = d[0];
    let finalRuppes = "";
    for (let i = 0; i < d[1].length; ++i) {
        if (d[1][i] != 'A') finalRuppes = finalRuppes + d[1][i];
        else break;
    }
    totalRupeeincart += parseInt(finalRuppes);
    let showinfoE = document.getElementById("showinfo");
    let itemCountSpan = document.getElementById("cart-item-count");
    itemCountSpan.textContent = numberOfItemsinCart + " items";
    let finalRuppesSpan = document.getElementById("cart-item-finalR");
    finalRuppesSpan.innerText = rSign + totalRupeeincart;
    if (numberOfItemsinCart <=1) {
        showinfoE.style.display = "inline-block";
        showinfoE.style.backgroundColor = "green";
        itemCountSpan.style.display = "block";
        itemCountSpan.style.backgroundColor = "green";
        itemCountSpan.style.color = "white";
        itemCountSpan.style.fontWeight = "25px";
        finalRuppesSpan.style.display = "inline";
        finalRuppesSpan.style.backgroundColor = "green";
        finalRuppesSpan.style.color = "white";
    }


}
let addbutton = document.getElementsByClassName("add-button-class");
for (let i = 0; i < addbutton.length; ++i) {
    addbutton[i].addEventListener("click", function (event) {
        addtocart(event, i);
    }, false);
}

function subtractit(i) {
    --numberOfItemsinCart;
    let originalRNodes = document.getElementsByClassName("original-ruppes")[i];
    let d = originalRNodes.textContent.split(" ");
    let rSign = d[0];
    let finalRuppes = "";
    for (let i = 0; i < d[1].length; ++i) {
        if (d[1][i] != 'A') finalRuppes = finalRuppes + d[1][i];
        else break;
    }
    totalRupeeincart -= parseInt(finalRuppes);
    let showinfoE = document.getElementById("showinfo");
    let itemCountSpan = document.getElementById("cart-item-count");
    itemCountSpan.textContent = numberOfItemsinCart + " items";
    let finalRuppesSpan = document.getElementById("cart-item-finalR");
    finalRuppesSpan.innerText = rSign + totalRupeeincart;
    let currentbutton = document.getElementsByClassName("current-quantity")[i];
    let textpart = currentbutton.textContent - 1;
    currentbutton.textContent = textpart;
    if (numberOfItemsinCart == 0) {                     /* disable the quantity and prices in cart and show the old image*/
        showinfoE.style.display = "none";
        itemCountSpan.style.display = "none";
        finalRuppesSpan.style.display = "none";
        let toarb = document.getElementsByClassName("to-add-remove")[i];
        let toclosed = document.getElementsByClassName("add-button-class")[i];
        toclosed.style.display = "inline";
        toarb.style.display = "none";
        let oldr = document.getElementsByClassName("old-ruppee")[i];
        oldr.style.top = "-5.5vh";
        let mycartimage2 = document.getElementById("cart-id-2");
        mycartimage2.style.display = "inline";
    }
    if (currentbutton.textContent == 0) {
        let b=document.getElementsByClassName("to-add-remove")[i];              //remove add button
        b.style.display="none";
        let toshow=document.getElementsByClassName("add-button-class")[i];
        toshow.style.display="inline-block";
        let oldr = document.getElementsByClassName("old-ruppee")[i];
        oldr.style.top = "-5.5vh";

    }

}

function addit(i) {
    ++numberOfItemsinCart;
    let originalRNodes = document.getElementsByClassName("original-ruppes")[i];
    let d = originalRNodes.textContent.split(" ");
    let rSign = d[0];
    let finalRuppes = "";
    for (let i = 0; i < d[1].length; ++i) {
        if (d[1][i] != 'A') finalRuppes = finalRuppes + d[1][i];
        else break;
    }
    totalRupeeincart += parseInt(finalRuppes);
    let showinfoE = document.getElementById("showinfo");
    let itemCountSpan = document.getElementById("cart-item-count");
    itemCountSpan.textContent = numberOfItemsinCart + " items";
    let finalRuppesSpan = document.getElementById("cart-item-finalR");
    finalRuppesSpan.innerText = rSign + totalRupeeincart;
    let currentbutton = document.getElementsByClassName("current-quantity")[i];
    let textpart = parseInt(currentbutton.textContent) + 1;
    currentbutton.textContent = textpart;

}

let subtract_button = document.getElementsByClassName("to-subtract");
for (let i = 0; i < subtract_button.length; ++i) {
    subtract_button[i].addEventListener("click", function () {
        subtractit(i)
    }, false);

}

let add_button = document.getElementsByClassName("to-add");
for (let i = 0; i < add_button.length; ++i) {
    add_button[i].addEventListener("click", function () {
        addit(i);
    }, false);

}






// for (let i = 0; i < length; ++i) {
//     setTimeout(() => {
//     }, 1000);
//     let currentText = "Search" + seach_box_text_list[i];
//     let searchbox = document.getElementById("searchboxid");
//     searchbox.value = currentText;
// }
