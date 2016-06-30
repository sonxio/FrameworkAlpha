<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@page pageEncoding="UTF-8"%>


<sql:query var="categories" dataSource="jdbc/affablebean">
    SELECT * FROM category
</sql:query>

<div id="indexLeftColumn">
    <div id="welcomeText">
        <p>[ welcome text ]<!-- test to access context parameters -->
            categoryImagePath: ${initParam.categoryImagePath}
            productImagePath: ${initParam.productImagePath}
        </p>
    </div>
</div>

<div id="indexRightColumn">
    <!-- $ {categories}信息来自ControllerServlet.init,是从EJB加载的数据 -->
    <c:forEach var="category" items="${categories}">
        <div class="categoryBox">
            <a href="category?${category.id}">

                <span class="categoryLabelText">${category.name}</span>

                <img src="${initParam.categoryImagePath}${category.name}.jpg"
                     alt="${category.name}">
            </a>
        </div>
    </c:forEach>
    
</div>