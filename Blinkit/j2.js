let b=[2,4,5,7,12,15,20];
let c=b.filter((value,key,index)=>{
    return index;
})
console.log(c);