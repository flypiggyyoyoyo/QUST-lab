
const menuItems = document.querySelectorAll("nav a");
const contentArea = document.getElementById("content-area");
const templates = document.getElementById("templates");

menuItems.forEach(item => {
    item.addEventListener("click", function(event) {
        event.preventDefault(); // 阻止默认行为
        const targetId = this.getAttribute("data-target"); // 获取目标内容的 ID
        const targetContent = templates.querySelector(`#${targetId}`); // 获取模板中的内容
        if (targetContent) {
            contentArea.innerHTML = targetContent.innerHTML; // 更新内容框架
        }
    });
});
