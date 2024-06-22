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
    searchbox.style.width = "1000px";
    searchbox.style.marginLeft = "-0.5vw";
    searchbox.value = "Search for atta dal and more";
    searchbox.style.fontSize = "2vh";
    searchbox.style.color = "rgb(83, 83, 83)";
    searchbox.style.border = "border: 1px solid rgba(0, 0, 0, 0.04)";

}, false);

function addtocart(Event) {
    let mycartimage = document.getElementById("cart-id-2");
    mycartimage.style.display = "none";
    let b = document.createElement("span");
    let topNode=document.createTextNode(numberOfItemsinCart);
    let downNode=document.createTextNode("&#8377"+4);
    let parent=mycartimage.parentElement();
    mycartimage.innerText=topNode;


}
let addbutton = document.getElementsByClassName("add-button-class");
var numberOfItemsinCart=0;
for (let i = 0; i < addbutton.length; ++i) {
    numberOfItemsinCart=1;
    addbutton[i].addEventListener("click", addtocart, false);
}



// for (let i = 0; i < length; ++i) {
//     setTimeout(() => {
//     }, 1000);
//     let currentText = "Search" + seach_box_text_list[i];
//     let searchbox = document.getElementById("searchboxid");
//     searchbox.value = currentText;
// }
