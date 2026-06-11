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

                case "uploadFileDrop":
                    UploadFile();
                    break;

                default:
                    break;
            }
        }
        
    



}
