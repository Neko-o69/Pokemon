var fct = "onItemClicked( " + nType + ", '" + strFullPath + "');";
div.onclick = new Function(fct);

var table = createQueryTable(strTitle, strDescription, nType);
div.appendChild(table);
