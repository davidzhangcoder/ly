local people = {
    username = "张三",
    age = 18
}
-- 引入依赖
local template = require("resty.template")
-- 渲染，第一个参数是要渲染的html页面，第二个是渲染数据
template.render("testTemplateHtml.html", people)
