public class Etat {

    private String nom;
    private int nbTour = 0;

    public Etat() {
    }

    public void effet() {
        // logique de l'effet à implémenter
    }
    
}


using Microsoft.Exchange.WebServices.Data;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.IO;
using System.Web;
using System.Web.Services;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Xml;


public class QualityAccess
{
    public string strEmpCode { get; set; }
    public string strNickName { get; set; }
    public string strEmail { get; set; }
}

public class QualityItem
{
    public string strFullPath { get; set; }
    public string strName { get; set; }
    public string strDescription { get; set; }

    public string strParent { get; set; }
    public string strType { get; set; }
    public string strEMail { get; set; }

    public List<QualityAccess> lstAccess;

    public QualityItem()
    {
        lstAccess = new List<QualityAccess>();
    }
}

public class QualityAccessRight
{
    public string strFullPath { get; set; }
    public string strEmpCode { get; set; }
    public string strEmpNickName { get; set; }

    public string strEmail { get; set; }
    
}


public partial class QualityFolder : System.Web.UI.Page
{
    public String m_strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
    String m_strEventArgument = "";
    String m_strPostBackCtrl = "";

    protected void Page_Load(object sender, EventArgs e)
    {
        m_strEventArgument = Request["__EVENTARGUMENT"];
        m_strPostBackCtrl = Request.Params.Get("__EVENTTARGET");

        if (IsPostBack == false)
        {

            //*--------------------------------------------------------------------------------
            // Init
            //*--------------------------------------------------------------------------------
            lbOpen.Attributes.Add("onclick", "document.getElementById('" + DocFileUpload.ClientID + "').click(); return false;");
            hfFolderTree.Value = "";
            FillUserList();

            //*--------------------------------------------------------------------------------
            // check argument
            //*--------------------------------------------------------------------------------
            if (Request.QueryString["folder"] == null)
                gotoFolder("");
            else
            {
                if (Request.QueryString["file"] != null)
                {
                    hfSelectedFile.Value = Request.QueryString["file"].ToString();
                    hfSelectedType.Value = GetFileType(hfSelectedFile.Value);
                }
                String strFullPath = Request.QueryString["folder"].ToString();
                gotoFolder(strFullPath);
            }

        }
        else
        {
            switch (m_strPostBackCtrl)
            {
                case "executeMenu":
                    executeMenu(m_strEventArgument);
                    break;
                case "gotoFolder":
                    gotoFolder(m_strEventArgument);
                    break;
                case "uploadFile":
                    UploadFile();
                    break;
                case "downloadFile":
                    DownloadFile(m_strEventArgument);
                    break;

                case "deleteFile":
                    DeleteFile(m_strEventArgument);
                    break;

                default:
                    break;
            }
        }

    }

    protected String GetFileType(String strFile)
    {
        int nIndex = strFile.LastIndexOf('.');
        if (nIndex < 0) return "-1";
        string strExt = strFile.Substring(nIndex + 1).ToUpper();
        switch (strExt)
        {
            case "DOC":
            case "DOCX":
            case "DOTX":
                return "1"; // MSWORD

            case "XLS":
            case "XLSX":
                return "2"; // EXCEL

            case "PDF":
                return "3"; // PDF

            case "JPEG":
            case "JPG":
            case "GIF":
            case "BMP":
            case "PNG":
                return "4"; // Image

            case "PPT":
            case "PPTX":
                return "5"; // Power point

            case "ZIP":
            case "RAR":
                return "6"; // Fichier compressé

            case "MPP":
                return "7"; // Project

            case "XML":
                return "8"; // XML
            case "BIN":
                return "9"; // BIN
            case "DWG":
                return "10"; // BIN

            default:
                break;
        }

        return "-1";
    }


    protected void FillUserList()
    {
        DataTable dt = DBHelper.SQLOpen("select EMP_CODE, EMP_LAS_NAME, EMP_FIR_NAME from EMPLOYES where EMP_STATUS <> -1 and EMP_PW = 'NIDEK' order by EMP_NIC_NAME", m_strConnection);
        for (int i = 0; i < dt.Rows.Count; i++)
        {
            ListItem li = new ListItem();
            li.Text = DBHelper.GetString(i, "EMP_FIR_NAME", dt) + " " + DBHelper.GetString(i, "EMP_LAS_NAME", dt);
            li.Value = DBHelper.GetString(i, "EMP_CODE", dt);
            ddlUser.Items.Add(li);
        }

        //|------------------------------------------------------------------------------
        // Très important !
        // On ajoute une ligne vide pour que l'object soit visible dans javascript
        //|------------------------------------------------------------------------------
        DataTable dtUser = new DataTable();
        dtUser.TableName = "UserRightTable";
        dtUser.Columns.Add(new DataColumn("BD_EMP_CODE", typeof(string)));
        dtUser.Columns.Add(new DataColumn("EMP_FIR_NAME", typeof(string)));
        dtUser.Columns.Add(new DataColumn("EMP_LAS_NAME", typeof(string)));
        DataRow dr = dtUser.NewRow();
        dr[0] = "";
        dr[1] = "";
        dr[2] = "";
        dtUser.Rows.Add(dr);


        string strSQL = "select BD_EMP_CODE, EMP_FIR_NAME, EMP_LAS_NAME from BD_RIGHT inner join EMPLOYES on BD_RIGHT.BD_EMP_CODE = EMPLOYES.EMP_CODE";
        dt = DBHelper.SQLOpen(strSQL, m_strConnection);

        //dataTable1.Merge(dataTable2);
        dtUser.Merge(dt);
        dlRight.DataSource = dtUser;
        dlRight.DataBind();

    }

    public bool ShowMsgBox(String strMessage, String strTitle, String strAction)
    {
        lbMsgBoxTitle.Text = strTitle;
        lbMsgBoxMessage.Text = strMessage;
        hfMsgBoxAction.Value = strAction;
        MsgBoxUpdatePanel.Update();
        mpeMsgBox.Show();
        return true;
    }

    protected void gotoFolder(String strFullPath)
    {
        String strSQL = "";

        if (strFullPath == "")
            strSQL = "select * from BASE_DOCUMENTAIRES where BD_FOLDER_PARENT is NULL or BD_FOLDER_PARENT = '' or BD_FOLDER_PARENT = 'Quality' order by BD_TYPE, BD_ORDER";
        else
            strSQL = "select * from BASE_DOCUMENTAIRES where BD_FOLDER_PARENT = '" + strFullPath + "' order by BD_TYPE, BD_NAME ASC";

        hfFolderTree.Value = strFullPath;
        // Extract le dossier courant
        String strPath = strFullPath;
        int nIndex = strPath.LastIndexOf('/');
        if (nIndex >= 0)
            strPath = strPath.Substring(nIndex + 1);
        hfCurrentFolder.Value = strPath;

        DataTable dt = DBHelper.SQLOpen(strSQL, m_strConnection);
        dlFolder.DataSource = dt;
        dlFolder.DataBind();
        Session["BASE_DOCUMENTAIRES"] = dt;

    }

    protected void DeleteFile(String strFullPath)
    {
        String strPath = HttpContext.Current.Server.MapPath("Images\\") + strFullPath;
        try
        {
            FileInfo fi = new FileInfo(strPath);
            if (fi.Exists)
            {
                fi.Delete();
            }
        }
        catch (Exception ex)
        {
            ShowMsgBox(ex.Message, "SYSTEM ERROR", "MsgBox");
            return;
        }

        // Ok mise à jour dans la database
        String strSQL = "delete from  BASE_DOCUMENTAIRES where BD_FULL_PATH LIKE '" + strFullPath + "%'";
        if (DBHelper.SQLExecute(strSQL, m_strConnection) == false) return;
        String strParentFolder = hfFolderTree.Value;
        gotoFolder(strParentFolder);

    }

    protected void executeMenu(String strItem)
    {
        switch (strItem)
        {
            case "deleteFolder":
                ShowMsgBox("Vous allez supprimer le dossier " + hfCurrentFolder.Value + " et tout son contenu<br/>Voulez vous continuer ?", "ATTENTION !", "DeleteFolder");
                break;

            default:
                break;
        }
    }


    // Type de Item
    // -1 = Folder System
    // 0 = Folder
    // 1 = Doc
    // 2 = Xls
    // 3 = Pdf
    // 4 = Jpeg, jpg, png, bmp
    protected void dlFolder_ItemDataBound(object sender, DataListItemEventArgs e)
    {
        if (e.Item != null)
        {
            //HiddenField hfRowIndex = (HiddenField)e.Item.FindControl("hfRowIndex");
            //if (hfRowIndex == null) return;
            //hfRowIndex.Value = e.Item.ItemIndex.ToString();

            if (String.IsNullOrEmpty(hfSelectedFile.Value) == false)
            {
                String strFullPath = DataBinder.Eval(e.Item.DataItem, "BD_FULL_PATH").ToString();
                if (strFullPath == hfSelectedFile.Value)
                {
                    hfSelectedRow.Value = e.Item.ItemIndex.ToString();
                }
            }

            Image im = (Image)e.Item.FindControl("imgFileType");
            if (im == null) return;
            String strType = DataBinder.Eval(e.Item.DataItem, "BD_TYPE").ToString().ToUpper();
            switch (strType)
            {
                case "1":
                    im.ImageUrl = "~/Images/icon_word.png";
                    break;

                case "2":
                    im.ImageUrl = "~/Images/icon_excel.png";
                    break;

                case "3":
                    im.ImageUrl = "~/Images/icon_pdf.png";
                    break;

                case "4":
                    im.ImageUrl = "~/Images/icon_image.png";
                    break;

                case "5":
                    im.ImageUrl = "~/Images/icon_powerpoint.png";
                    break;

                case "6":
                    im.ImageUrl = "~/Images/icon_winrar.png";
                    break;

                case "7":
                    im.ImageUrl = "~/Images/icon_project.png";
                    break;
                case "8":
                    im.ImageUrl = "~/Images/icon_xml.png";
                    break;
                case "9":
                    im.ImageUrl = "~/Images/icon_bin.png";
                    break;
                case "10":
                    im.ImageUrl = "~/Images/icon_dwg.png";
                    break;

                default:
                    im.ImageUrl = "~/Images/icon_folder_documents.png";
                    break;
            }

        }
    }

    protected void DownloadFile(string strFileName)
    {
        String strFile = Server.MapPath("Images/") + strFileName.Replace('/', '\\');
        int nIndex = strFile.LastIndexOf('\\');
        String strName = Uri.EscapeDataString(strFile.Substring(nIndex + 1));

        //Response.Clear();
        //Response.ClearHeaders();
        //Response.ClearContent();
        //Response.AddHeader("Content-Disposition", "attachment; filename=\"" + strName + "\"");
        //Response.AddHeader("Content-Length", strName.Length.ToString());
        //Response.ContentType = "text/plain";
        //Response.Flush();
        //Response.TransmitFile(strFile);
        //Response.End();

        Response.Clear();
        Response.ClearHeaders();
        Response.ClearContent();
        //switch (hfSelectedType.Value)
        //{
        //    case "1":   // word
        //        Response.ContentType = "application/ms-word";
        //        break;
        //    case "2": // excel
        //        Response.ContentType = "application/pdf";
        //        break;
        //    default:
        //        Response.ContentType = "application/pdf";
        //        break;
        //}

        Response.AppendHeader("Content-Disposition", "attachment; filename=" + strName);

        // Write the file to the Response
        const int bufferLength = 10000;
        byte[] buffer = new Byte[bufferLength];
        int length = 0;
        Stream download = null;
        try
        {
            download = new FileStream(strFile, FileMode.Open, FileAccess.Read);
            Response.AddHeader("Content-Length", download.Length.ToString());
            Response.ContentType = "application/octet-stream";

            do
            {
                if (Response.IsClientConnected)
                {
                    length = download.Read(buffer, 0, bufferLength);
                    Response.OutputStream.Write(buffer, 0, length);
                    buffer = new Byte[bufferLength];
                }
                else
                {
                    length = -1;
                }
            }
            while (length > 0);
            Response.Flush();
            Response.End();
        }
        finally
        {
            if (download != null)
                download.Close();
        }

    }

    protected void UploadFile()
    {

        NKUser nkUser = (NKUser)Session["User"];
        if (nkUser.HasRight(NKUser.USER_RIGHT_UPLOAD) == false)
        {
            ShowMsgBox("Vous n'avez pas le droit pour cette opération", "ERREUR !", "MsgBox");
            return;
        }
        if (DocFileUpload.HasFile == false) return;

        // Save this file to temps folder
        String strDate = DBHelper.GetCurrentDate();
        String strParentFolder = hfFolderTree.Value;
        if (String.IsNullOrEmpty(strParentFolder))
            strParentFolder = "Quality";
        String strFullPath = strParentFolder + "/" + DocFileUpload.FileName.Replace("'", "&quote");
        String strFileName = Server.MapPath("Images/") + strFullPath;

        int nIndex = DocFileUpload.FileName.LastIndexOf('.');
        if (nIndex < 0) return;
        String strExt = DocFileUpload.FileName.Substring(nIndex + 1).ToUpper();
        String strDisplay = tbDisplay.Text;
        if (strDisplay == "")
            strDisplay = DocFileUpload.FileName.Substring(0, nIndex);
        String strDescription = tbDescription.Text;

        switch (strExt)
        {
            case "DOC":
            case "DOCX":
                nIndex = 1;
                break;

            case "XLS":
            case "XLSX":
                nIndex = 2;
                break;

            case "PDF":
                nIndex = 3;
                break;

            case "PNG":
            case "JPG":
            case "JPEG":
            case "GIF":
            case "BMP":
                nIndex = 4;
                break;

            case "PPT":
            case "PPTX":
                nIndex = 5;
                break;

            case "ZIP":
            case "RAR":
                nIndex = 6;
                break;

            case "MPP":
                nIndex = 7;
                break;

            case "XML":
                nIndex = 8;
                break;
            case "BIN":
                nIndex = 9;
                break;
            case "DWG":
                nIndex = 10;
                break;

            default:
                ShowMsgBox("Le type de fichier [" + strExt + "] n'est pas connu !\r\nLes fichiers valables sont Word, Pdf, Excel, Images, Zip.", "ERREUR !", "MsgBox");
                return;

        }

        try
        {
            FileInfo fi = new FileInfo(strFileName);
            if (fi.Exists)
            {
                fi.Delete();
            }
            DocFileUpload.SaveAs(strFileName);

            // Save ok, insert into database
            String strParent = strParentFolder;
            //if (strParent == "Quality") strParent = ""; // Suis à la racine !
            int nOrder = 0;
            DataTable dt;
            if (strParent == "")
                dt = DBHelper.SQLOpen("select BD_ORDER from BASE_DOCUMENTAIRES " +
                    "where BD_FOLDER_PARENT is NULL or BD_FOLDER_PARENT = '' or BD_FOLDER_PARENT = 'Quality' order by BD_ORDER desc", m_strConnection);
            else
                dt = DBHelper.SQLOpen("select BD_ORDER from BASE_DOCUMENTAIRES where BD_FOLDER_PARENT = '" + strParent + "' order by BD_ORDER desc", m_strConnection);

            if (dt.Rows.Count > 0)
                nOrder = DBHelper.GetInt(0, "BD_ORDER", dt) + 1;


            String strSQL = "insert into BASE_DOCUMENTAIRES" +
                "(BD_FULL_PATH, BD_NAME, BD_DESCRIPTION, BD_FOLDER_PARENT, BD_CREATE_DATE, BD_CREATE_USER, BD_MODIFY_DATE, BD_MODIFY_USER, BD_TYPE, BD_ORDER) values (" +
                "'" + DBHelper.CharToSQL(strFullPath) + "', '" + DBHelper.CharToSQL(strDisplay) + "', '" + DBHelper.CharToSQL(strDescription) + "', '" + strParent + "', " +
                strDate + ", '" + nkUser.m_strCode + "', NULL, NULL, " + nIndex.ToString() + ", " + nOrder.ToString() + ")";
            if (DBHelper.SQLExecute(strSQL, m_strConnection) == false)
            {
                fi.Delete();
                ShowMsgBox("System error please contact admin for more details !", "ERROR !", "MsgBox");
                return;
            }



            // Reload...
            gotoFolder(strParentFolder);
            //if(dlFolder.Items.Count <= 0)
            //{
            //    hfAddNewFile.Value = "";
            //    gotoFolder(strParentFolder);
            //}
            //else
            //    hfAddNewFile.Value = strDisplay + "/" + nIndex.ToString() + "/" + strFullPath;
        }
        catch (Exception ex)
        {
            ShowMsgBox(ex.Message, "ERREUR SYSTEME !", "MsgBox");
        }

    }





    //|--------------------------------------------------------------
    // Les WEB Services
    //|--------------------------------------------------------------
    [WebMethod(EnableSession = true)]
    public static string addNewFolder(string strParentFolder, string strFolder, string strDisplay, string strDescription)
    {
        String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
        if (HttpContext.Current.Session["User"] == null) return "ERROR, no login user !";
        NKUser nkUser = (NKUser)HttpContext.Current.Session["User"];

        if (String.IsNullOrEmpty(strParentFolder))
            return "ERROR ! Parent Folder name is empty !";

        if (String.IsNullOrEmpty(strFolder))
            return "ERROR ! Folder name is empty !";

        // Check if this folder already exist !
        //String strPath = "~/Images/" + strParentFolder + "/" + strFolder;
        String strPath = HttpContext.Current.Server.MapPath("Images/") + strParentFolder + "\\" + strFolder;

        if (NKTools.IsFolderExist(strPath))
            return "This folder is already exist !";

        // Ok créer maintenant
        try
        {
            Directory.CreateDirectory(strPath);
        }
        catch (Exception ex)
        {
            return "ERROR : " + ex.Message;
        }

        String strParent = strParentFolder;
        if (strParent == "Quality") strParent = ""; // Suis à la racine !
        int nOrder = 0;
        DataTable dt;
        if (strParent == "")
            dt = DBHelper.SQLOpen("select BD_ORDER from BASE_DOCUMENTAIRES where BD_FOLDER_PARENT is NULL or BD_FOLDER_PARENT = '' order by BD_ORDER desc", strConnection);
        else
            dt = DBHelper.SQLOpen("select BD_ORDER from BASE_DOCUMENTAIRES where BD_FOLDER_PARENT = '" + strParent + "' order by BD_ORDER desc", strConnection);

        if (dt.Rows.Count > 0)
            nOrder = DBHelper.GetInt(0, "BD_ORDER", dt) + 1;

        // Ok mise à jour dans la database

        strPath = strParentFolder + "/" + strFolder;
        String strDate = DBHelper.GetCurrentDate();
        String strSQL = "insert into BASE_DOCUMENTAIRES" +
            "(BD_FULL_PATH, BD_NAME, BD_DESCRIPTION, BD_FOLDER_PARENT, BD_CREATE_DATE, BD_CREATE_USER, BD_MODIFY_DATE, BD_MODIFY_USER, BD_TYPE, BD_ORDER) values (" +
            "'" + strPath + "', '" + DBHelper.CharToSQL(strDisplay) + "', '" + DBHelper.CharToSQL(strDescription) + "', '" + strParent + "', " +
            strDate + ", '" + nkUser.m_strCode + "', NULL, NULL, 0, " + nOrder.ToString() + ")";
        if (DBHelper.SQLExecute(strSQL, strConnection) == false)
        {
            Directory.Delete(strPath);
            return "System error please contact admin for more details !";
        }

        return "true";
    }

    public static string DeleteDir(string strFolder)
    {
        String strRetCode;
        String strSQL;
        int nIndex;
        String strFullPath;
        String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
        try
        {
            DirectoryInfo dir = new DirectoryInfo(strFolder);
            foreach (FileInfo fi in dir.GetFiles())
            {
                // Ok mise à jour dans la database        
                nIndex = fi.FullName.IndexOf("\\Quality");
                strFullPath = fi.FullName.Substring(nIndex + 1);
                strFullPath = strFullPath.Replace('\\', '/');
                strSQL = "delete from  BASE_DOCUMENTAIRES where BD_FULL_PATH = '" + strFullPath + "'";
                if (DBHelper.SQLExecute(strSQL, strConnection) == false)
                    return "System error please contact admin for more details !";

                fi.Delete();


            }
            foreach (DirectoryInfo di in dir.GetDirectories())
            {
                strRetCode = DeleteDir(di.FullName);
                if (strRetCode != "true") return strRetCode;
            }

            // Finalement
            dir.Delete();

        }
        catch (Exception ex)
        {
            return "ERROR : " + ex.Message;
        }

        // Ok mise à jour dans la database
        nIndex = strFolder.IndexOf("\\Quality");
        strFullPath = strFolder.Substring(nIndex + 1);
        strFullPath = strFullPath.Replace('\\', '/');
        strSQL = "delete from  BASE_DOCUMENTAIRES where BD_FULL_PATH = '" + strFullPath + "'";
        if (DBHelper.SQLExecute(strSQL, strConnection) == false)
            return "System error please contact admin for more details !";

        return "true";
    }

    [WebMethod(EnableSession = true)]
    public static string deleteFolder(string strFullPath)
    {
        String strPath = HttpContext.Current.Server.MapPath("Images\\") + strFullPath;
        String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
        if (Directory.Exists(strPath))
        {
            DBHelper.SQLExecute("delete from BASE_DOCUMENTAIRES where BD_FULL_PATH LIKE '" + strFullPath + "'", strConnection);
            return "true";
        }

            return DeleteDir(strPath);
    }


    //|---------------------------------------------------------
    // This run only one shoot, cause index not re sort !
    // Abandon, mais keep this in mind, maybe userful later
    //|---------------------------------------------------------
    [WebMethod(EnableSession = true)]
    public static string deleteFile(string strFullPath)
    {

        String strPath = HttpContext.Current.Server.MapPath("Images\\") + strFullPath;
        String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
        try
        {
            FileInfo fi = new FileInfo(strPath);
            if (fi.Exists)
            {
                fi.Delete();
            }
        }
        catch (Exception ex)
        {
            return "ERROR : " + ex.Message;
        }

        // Ok mise à jour dans la database
        String strSQL = "delete from  BASE_DOCUMENTAIRES where BD_FULL_PATH = '" + strFullPath + "'";
        if (DBHelper.SQLExecute(strSQL, strConnection) == false)
            return "System error please contact admin for more details !";

        return "true";
    }

    [WebMethod(EnableSession = true)]
    public static string moveLineUp(string strLineNumber)
    {
        String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
        if (HttpContext.Current.Session["BASE_DOCUMENTAIRES"] == null) return "ERROR ! Session variable not found !";
        DataTable dt = (DataTable)HttpContext.Current.Session["BASE_DOCUMENTAIRES"];

        String strOriginValue, strTargetValue;
        int nOriginOrder, nTargetOrder;

        int nLineNumber = -1;
        int.TryParse(strLineNumber, out nLineNumber);

        if (nLineNumber < 0 || nLineNumber >= dt.Rows.Count)
            return "false";

        if (nLineNumber == 0 || dt.Rows.Count == 1)
            return "";

        strOriginValue = DBHelper.GetString(nLineNumber, "BD_FULL_PATH", dt);
        nOriginOrder = DBHelper.GetInt(nLineNumber, "BD_ORDER", dt);

        strTargetValue = DBHelper.GetString(nLineNumber - 1, "BD_FULL_PATH", dt);
        nTargetOrder = DBHelper.GetInt(nLineNumber - 1, "BD_ORDER", dt);

        // Swap
        dt.Rows[nLineNumber]["BD_ORDER"] = nTargetOrder;
        dt.Rows[nLineNumber - 1]["BD_ORDER"] = nOriginOrder;
        DataRow tempRow = dt.NewRow();
        for (int i = 0; i < dt.Columns.Count; i++)
        {
            tempRow[i] = dt.Rows[nLineNumber][i];
            dt.Rows[nLineNumber][i] = dt.Rows[nLineNumber - 1][i];
            dt.Rows[nLineNumber - 1][i] = tempRow[i];
        }

        HttpContext.Current.Session["BASE_DOCUMENTAIRES"] = dt;

        // Ok update now database
        String strSQL = "update BASE_DOCUMENTAIRES set BD_ORDER = " + nTargetOrder.ToString() + " where BD_FULL_PATH = '" + strOriginValue + "'";
        if (DBHelper.SQLExecute(strSQL, strConnection) == false)
            return "System error please contact admin for more details !";
        strSQL = "update BASE_DOCUMENTAIRES set BD_ORDER = " + nOriginOrder.ToString() + " where BD_FULL_PATH = '" + strTargetValue + "'";
        if (DBHelper.SQLExecute(strSQL, strConnection) == false)
            return "System error please contact admin for more details !";

        return "true";
    }

    [WebMethod(EnableSession = true)]
    public static string moveLineDown(string strLineNumber)
    {
        String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
        if (HttpContext.Current.Session["BASE_DOCUMENTAIRES"] == null) return "ERROR ! Session variable not found !";
        DataTable dt = (DataTable)HttpContext.Current.Session["BASE_DOCUMENTAIRES"];

        String strOriginValue, strTargetValue;
        int nOriginOrder, nTargetOrder;

        int nLineNumber = -1;
        int.TryParse(strLineNumber, out nLineNumber);

        if (nLineNumber < 0 || nLineNumber >= dt.Rows.Count)
            return "false";

        if (nLineNumber == dt.Rows.Count - 1 || dt.Rows.Count == 1)
            return "";

        strOriginValue = DBHelper.GetString(nLineNumber, "BD_FULL_PATH", dt);
        nOriginOrder = DBHelper.GetInt(nLineNumber, "BD_ORDER", dt);

        strTargetValue = DBHelper.GetString(nLineNumber + 1, "BD_FULL_PATH", dt);
        nTargetOrder = DBHelper.GetInt(nLineNumber + 1, "BD_ORDER", dt);

        // Swap
        dt.Rows[nLineNumber]["BD_ORDER"] = nTargetOrder;
        dt.Rows[nLineNumber + 1]["BD_ORDER"] = nOriginOrder;
        DataRow tempRow = dt.NewRow();
        for (int i = 0; i < dt.Columns.Count; i++)
        {
            tempRow[i] = dt.Rows[nLineNumber][i];
            dt.Rows[nLineNumber][i] = dt.Rows[nLineNumber + 1][i];
            dt.Rows[nLineNumber + 1][i] = tempRow[i];
        }

        HttpContext.Current.Session["BASE_DOCUMENTAIRES"] = dt;

        // Ok update now database
        String strSQL = "update BASE_DOCUMENTAIRES set BD_ORDER = " + nTargetOrder.ToString() + " where BD_FULL_PATH = '" + strOriginValue + "'";
        if (DBHelper.SQLExecute(strSQL, strConnection) == false)
            return "System error please contact admin for more details !";
        strSQL = "update BASE_DOCUMENTAIRES set BD_ORDER = " + nOriginOrder.ToString() + " where BD_FULL_PATH = '" + strTargetValue + "'";
        if (DBHelper.SQLExecute(strSQL, strConnection) == false)
            return "System error please contact admin for more details !";

        return "true";
    }

    [WebMethod(EnableSession = true)]
    public static string requestFolderInformation(String strFullPath)
    {
        if (HttpContext.Current.Session["User"] == null) return "ERROR ! User not login !";
        String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;

        // Cherche le REP parent !
        String strParent = "";
        int nIndex = strFullPath.IndexOf('/', 9); // 9 car on ignore le 'Quality/'
        if (nIndex < 0) strParent = strFullPath;
        else
        {
            strParent = strFullPath.Substring(0, nIndex);
        }

        DataTable dt = DBHelper.SQLOpen("select * from BD_EMAIL where BD_FULL_PATH = '" + strParent + "'", strConnection);
        if (dt.Rows.Count <= 0) return "ERROR ! No email associate for this folder !";
        NKUser nkUser = (NKUser)HttpContext.Current.Session["User"];
        return DBHelper.GetString(0, "BD_EMAIL", dt) + "/" + nkUser.m_strEMail;
    }

    [WebMethod(EnableSession = true)]
    public static string sendRequestModification(string strTo, string strObject, string strBody, string strAttach)
    {
        String strUser, strPW, strServer, strAuthenfification, strXmlFile, strAttachFile, strEMail;
        XmlDocument xmlFile = new XmlDocument();
        XmlNodeList xlmItems;

        if (HttpContext.Current.Session["User"] == null) return "ERROR ! User not login !";
        NKUser nkUser = (NKUser)HttpContext.Current.Session["User"];
        strXmlFile = HttpContext.Current.Server.MapPath("App_Data") + "\\Users\\" + nkUser.m_strCode + "\\SmtpServer.xml";
        if (NKTools.IsFileExist(strXmlFile) == false) return "ERROR ! Configuration file for email not found ";
        xmlFile.Load(strXmlFile);

        xlmItems = xmlFile.GetElementsByTagName("Authentification");
        strAuthenfification = xlmItems[0].InnerText.ToString().ToUpper();

        xlmItems = xmlFile.GetElementsByTagName("User");
        strUser = xlmItems[0].InnerText.ToString();

        xlmItems = xmlFile.GetElementsByTagName("MW");
        strPW = xlmItems[0].InnerText.ToString();

        xlmItems = xmlFile.GetElementsByTagName("SMTP");
        strServer = xlmItems[0].InnerText.ToString();


        // Reformatter...
        String strText = strBody.Replace('|', '\'');


        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP1);
        service.Credentials = new WebCredentials(strUser, strPW);
        //service.Url = new Uri( "https://mail.nidek.fr/EWS/Exchange.asmx" );
        service.Url = new Uri(strServer);
        EmailMessage message = new EmailMessage(service);
        message.Subject = strObject;
        //message.Body = MessageTextBox.Text;
        message.Body = strText.Replace("\r\n", "<br/>");

        //|*******************************************************************
        //|-- Multi destiny
        int nOffset = 0;
        int nIndex = strTo.IndexOf(';');
        while (nIndex > 0)
        {
            strEMail = strTo.Substring(nOffset, nIndex - nOffset).Trim();
            if (String.IsNullOrEmpty(strEMail) == false)
                message.ToRecipients.Add(strEMail);

            nOffset += (nIndex - nOffset) + 1;
            nIndex = strTo.IndexOf(';', nOffset);
        }
        // Last item;
        strEMail = strTo.Substring(nOffset).Trim();
        if (String.IsNullOrEmpty(strEMail) == false)
            message.ToRecipients.Add(strEMail);
        //|*******************************************************************

        // Cc tjs à audrey
        String strCC = ConfigurationManager.AppSettings["QualityManager"];
        if (String.IsNullOrEmpty(strCC) == false)
        {
            message.CcRecipients.Add(strCC);
        }

        // Attachment
        strAttachFile = HttpContext.Current.Server.MapPath("Images\\") + strAttach;
        strAttachFile = strAttachFile.Replace("/", "\\");
        if (NKTools.IsFileExist(strAttachFile) == false)
            return "ERROR ! Attach file not exist";

        message.Attachments.AddFileAttachment(strAttachFile);
        try
        {
            message.Save();
            message.SendAndSaveCopy();
        }
        catch (Exception ex)
        {
            return "ERROR : " + ex.ToString();
        }

        return "true";
    }

    [WebMethod(EnableSession = true)]
    public static string loadUserRight(string strFullPath)
    {
        String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
        NKUser nkUser = (NKUser)HttpContext.Current.Session["User"];
        if (nkUser != null && nkUser.HasRight(NKUser.USER_RIGHT_QUALITY))
        {
            return "admin";
        }

        if (String.IsNullOrEmpty(strFullPath)) return "false";

        // Sinon on décompose le full path
        List<string> lstPath = new List<string>();
        int nIndex = strFullPath.IndexOf('/');
        if (nIndex < 0) return "false"; // Nothing to do...
        String strData, strPath = strFullPath.Substring(nIndex + 1);
        strData = "Quality";
        nIndex = strPath.IndexOf('/');
        while (nIndex != -1)
        {
            strData += "/" + strPath.Substring(0, nIndex);
            lstPath.Add(strData);
            strPath = strPath.Substring(nIndex + 1);
            nIndex = strPath.IndexOf('/');
        }
        // Last element
        strData += "/" + strPath;
        lstPath.Add(strData);

        // Construire le SQL
        String strSQL = "select * from BD_RIGHT where BD_EMP_CODE = '" + nkUser.m_strCode + "' and (";
        nIndex = 0;
        foreach (String strItem in lstPath)
        {
            if (nIndex == 0)
            {
                strSQL += "BD_FULL_PATH = '" + strItem + "'";
                nIndex = 1;
            }
            else
            {
                strSQL += " or BD_FULL_PATH = '" + strItem + "'";
            }
        }
        strSQL += ")";

        DataTable dt = DBHelper.SQLOpen(strSQL, strConnection);
        if (dt.Rows.Count > 0)
            return "true";

        return "false";

    }

    [WebMethod(EnableSession = true)]
    public static List<QualityItem> requestProperties(string strFullPath)
    {
        String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
        List<QualityItem> lstItem = new List<QualityItem>();

        String strSQL = "select BASE_DOCUMENTAIRES.*, BD_EMAIL.BD_EMAIL from BASE_DOCUMENTAIRES left join BD_EMAIL on BASE_DOCUMENTAIRES.BD_FULL_PATH = BD_EMAIL.BD_FULL_PATH " +
            "where BASE_DOCUMENTAIRES.BD_FULL_PATH = '" + strFullPath + "'";

        DataTable dt = DBHelper.SQLOpen(strSQL, strConnection);

        if (dt.Rows.Count > 0)
        {
            QualityItem item = new QualityItem();
            item.strFullPath = strFullPath;
            item.strName = DBHelper.GetString(0, "BD_NAME", dt);
            item.strDescription = DBHelper.GetString(0, "BD_DESCRIPTION", dt);

            item.strParent = DBHelper.GetString(0, "BD_FOLDER_PARENT", dt);
            item.strType = DBHelper.GetAbsoluteString(0, "BD_TYPE", dt);
            item.strEMail = DBHelper.GetString(0, "BD_EMAIL", dt);

            // Mantenant liste des droit
            strSQL = "select BD_RIGHT.*, EMPLOYES.EMP_NIC_NAME, EMPLOYES.EMP_EMAIL from BD_RIGHT inner join EMPLOYES on BD_RIGHT.BD_EMP_CODE = EMPLOYES.EMP_CODE " +
            "where BD_RIGHT.BD_FULL_PATH = '" + strFullPath + "'";
            dt = DBHelper.SQLOpen(strSQL, strConnection);
            for (int i = 0; i < dt.Rows.Count; i++)
            {
                QualityAccess qa = new QualityAccess();
                qa.strEmpCode = DBHelper.GetString(i, "BD_EMP_CODE", dt);
                qa.strNickName = DBHelper.GetString(i, "EMP_NIC_NAME", dt);
                qa.strEmail = DBHelper.GetString(i, "EMP_EMAIL", dt);
                item.lstAccess.Add(qa);
            }

            lstItem.Add(item);
        }

        return lstItem;
    }

    [WebMethod(EnableSession = true)]
    public static string requestSaveProperties(string strData)
    {
        String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
        List<QualityItem> lstItem = JsonConvert.DeserializeObject<List<QualityItem>>(strData);

        String strName = lstItem[0].strName.Replace("&quote", "'");
        String strDescription = lstItem[0].strDescription.Replace("&quote", "'");


        String strSQL = "update BASE_DOCUMENTAIRES set BD_NAME = '" + DBHelper.CharToSQL(strName)
            + "', BD_DESCRIPTION = '" + DBHelper.CharToSQL(strDescription) + "' where BD_FULL_PATH = '" + lstItem[0].strFullPath + "'";


        if (DBHelper.SQLExecute(strSQL, strConnection) == false) return "false";


        strSQL = "UPDATE BASE_DOCUMENTAIRES "
        + "SET BD_FULL_PATH = REPLACE(BD_FULL_PATH, '" + lstItem[0].strFullPath + "', '" + lstItem[0].strParent + "/" + lstItem[0].strName + "') "
        + "where BD_FULL_PATH like '" + lstItem[0].strFullPath  + "'";

        if (DBHelper.SQLExecute(strSQL, strConnection) == false) return "false";

        strSQL = "UPDATE BASE_DOCUMENTAIRES "
        + "SET BD_FOLDER_PARENT = REPLACE(BD_FOLDER_PARENT, '" + lstItem[0].strFullPath + "', '" + lstItem[0].strParent + "/" + lstItem[0].strName + "') "
        + "where BD_FULL_PATH like '" + lstItem[0].strParent + "/" + lstItem[0].strName + "'";

        if (DBHelper.SQLExecute(strSQL, strConnection) == false) return "false";



        String strPath = ConfigurationManager.AppSettings["PATH"];

        //string strSource = "C:/inetpub/wwwroot/Quality/Images/" + lstItem[0].strFullPath;
        //string strDestination = "C:/inetpub/wwwroot/Quality/Images/" + lstItem[0].strParent + "/" + lstItem[0].strName;

        string strSource = strPath + lstItem[0].strFullPath;
        string strDestination = strPath + lstItem[0].strParent + '/' + lstItem[0].strName;
       
        try
        {
            File.Move(strSource, strDestination);
        

        }
        catch (Exception e)
        {
            Console.WriteLine(e.Message);
        }
        // Update aussi l'email de notification ssi droit Ok
        NKUser nkUser = (NKUser)HttpContext.Current.Session["User"];
        if (nkUser.HasRight(NKUser.USER_RIGHT_QUALITY))
        {
            strSQL = "update BD_EMAIL set BD_EMAIL.BD_EMAIL = '" + lstItem[0].strEMail
                        + "' where BD_FULL_PATH = '" + lstItem[0].strFullPath + "'";

           if (DBHelper.SQLExecute(strSQL, strConnection) == false) return "false";
        }

        return "true";
    }

    [WebMethod]
    public static List<QualityAccessRight> requestAccessList(string strFullPath)
    {
        List<QualityAccessRight> lstItem = new List<QualityAccessRight>();
        String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
        DataTable dt = DBHelper.SQLOpen("select BD_RIGHT.*, EMPLOYES.EMP_FIR_NAME, EMPLOYES.EMP_LAS_NAME from BD_RIGHT inner join EMPLOYES on BD_RIGHT.BD_EMP_CODE = EMPLOYES.EMP_CODE " +
            "where BD_FULL_PATH = '" + strFullPath + "'", strConnection);

        for (int i = 0; i < dt.Rows.Count; i++)
        {
            QualityAccessRight item = new QualityAccessRight();
            item.strFullPath = strFullPath;
            item.strEmpCode = DBHelper.GetString(i, "BD_EMP_CODE", dt);
            item.strEmpNickName = DBHelper.GetString(i, "EMP_FIR_NAME", dt) + " " + DBHelper.GetString(i, "EMP_LAS_NAME", dt);
            lstItem.Add(item);
        }

        return lstItem;
    }

    [WebMethod]
    public static string requestAddUser(string strFullPath, string strEmpCode)
    {
        String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;
        // User exist ?
        DataTable dt = DBHelper.SQLOpen("select * from BD_RIGHT where BD_FULL_PATH = '" + strFullPath +
            "' and BD_EMP_CODE = '" + strEmpCode + "'", strConnection);
        if (dt.Rows.Count > 0) return "false";

        // Ok, add to data base
        if (DBHelper.SQLExecute("insert into BD_RIGHT(BD_FULL_PATH, BD_EMP_CODE, BD_RIGHT) values (" +
            "'" + strFullPath + "', '" + strEmpCode + "', 65535 )", strConnection) == false)
            return "false";

        return "true";
    }

    [WebMethod]
    public static string requestDeleteUser(string strFullPath, string strEmpCode)
    {
        String strConnection = ConfigurationManager.ConnectionStrings["OPHELIE"].ConnectionString;

        // Ok, add to data base
        if (DBHelper.SQLExecute("delete from BD_RIGHT where BD_FULL_PATH = '" + strFullPath + "' and BD_EMP_CODE = '" +
            strEmpCode + "'", strConnection) == false)
            return "false";

        return "true";
    }

}
