function requestSaveProperties() {
    
    

    if (objPropertiesItem == null) return;
    // Ok l'objet json
    var tb = document.getElementById('mainContent_tbItemName');
    if (tb) objPropertiesItem[0].strName = tb.value;
    objPropertiesItem[0].strName = objPropertiesItem[0].strName.replace(/'/g, "\u2019");

    tb = document.getElementById('mainContent_tbItemEMail');
    if (tb) objPropertiesItem[0].strEMail = tb.value;

    tb = document.getElementById('mainContent_tbItemDescription');
    if (tb) objPropertiesItem[0].strDescription = tb.value;
    var des = objPropertiesItem[0].strDescription.replace(/'/g, "\u2019");
    objPropertiesItem[0].strDescription = objPropertiesItem[0].strDescription.replace(/'/g, "\u2019");

    var strData =
        JSON.stringify(objPropertiesItem); //|-- Serialize
    closeDialog('mainContent_mpeProperties');
    
    
    $.ajax({
        type: "POST",
        url: "QualityFolder.aspx/requestSaveProperties",
        data: JSON.stringify({strData: strData}),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: onRequestSavePropertiesOk,
        failure: onError
        
    });
}

function onRequestSavePropertiesOk(response) {
    if (response.d == null) return;
    if (response.d != "true") return;
   

    // Ok update element
    if (objLastSelected == null) return;
    var div = objLastSelected;
    var subTable = div.getElementsByTagName('TABLE')[0];
    // (row 0, col 1) = nom du dossier ou fichier
    subTable.rows[0].children[0].innerText = objPropertiesItem[0].strName;
    location.reload();
    
}
[WebMethod(EnableSession = true)]
public static string requestSaveProperties(string strData)
{
    String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
    List<QualityItem> lstItem = JsonConvert.DeserializeObject<List<QualityItem>>(strData);
    String strName = lstItem[0].strName.Replace("&quote", "'");
    String strDescription = lstItem[0].strDescription.Replace("&quote", "'");

    String strSQL = "update BASE_DOCUMENTAIRES set BD_NAME = '" + DBHelper.CharToSQL(strName)
        + "', BD_DESCRIPTION = '" + DBHelper.CharToSQL(strDescription) + "' where BD_FULL_PATH = '" + lstItem[0].StrFullPath + "'";
    if (DBHelper.SQLExecute(strSQL, strConnection) == false) return "false";

    NKUser nkUser = (NKUser)HttpContext.Current.Session["User"];
    if (nkUser.HasRight(NKUser.USER_RIGHT_QUALITY))
    {
        strSQL = "update BD_EMAIL set BD_EMAIL.BD_EMAIL = '" + lstItem[0].strEMail
                    + "' where BD_FULL_PATH = '" + lstItem[0].StrFullPath + "'";
        if (DBHelper.SQLExecute(strSQL, strConnection) == false) return "false";
    }
    return "true";
}
