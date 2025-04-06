export function togglePassword(e){
    e.preventDefault();
    console.log(e.target.parentNode);

        let parent = e.target.parentNode;
        let passField = parent.getElementsByTagName("input")[0];
        let passBtn = parent.getElementsByTagName("button")[0];

        if(passField.type === "password"){
            passField.type = "text";
            passBtn.textContent = "Hide";
        }
        else{
            passField.type = "password";
            passBtn.textContent = "Show";
        }
    }


export function toggleDropdown(e){
    
    let dropdown = document.getElementById("sort-dropdown");
    let button = document.getElementById("sort-menu");

    dropdown.classList.toggle('hidden');

    let isExpanded = button.getAttribute("aria-expanded") === true;
    button.setAttribute("aria-expanded", !isExpanded);

}

export function showOptions(e){
    e.preventDefault();
    let target = e.target;
    let parent = target.parentNode;
    let options = parent.getElementsByTagName("div")[0];
    console.log(options);
    if(options.classList.contains("hidden")){
        options.classList.toggle("hidden", false);
    }
    else{
        options.classList.toggle("hidden", true);
    }
}