
const menuItems = document.querySelectorAll("nav a");
const contentArea = document.getElementById("content-area");
const templates = document.getElementById("templates");

menuItems.forEach(item => {
    item.addEventListener("click", function(event) {
        event.preventDefault(); 
        const targetId = this.getAttribute("data-target"); 
        const targetContent = templates.querySelector(`#${targetId}`); 
        if (targetContent) {
            contentArea.innerHTML = targetContent.innerHTML; 
        }
    });
});
