catch (Exception ex)
{
    String strDetail = ex.Message;
    if (ex.InnerException != null) strDetail += " | " + ex.InnerException.Message;
    return "ERROR : " + strDetail;
}
