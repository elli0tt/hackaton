namespace WebApi.Models.Support
{
    public class PatientInfo
    {
        public long Id { get; set; }
        public string Username { get; set; }
        public string FullName { get; set; }
        public string SNILS { get; set; }
        public long DateOfReg { get; set; }

        public PatientInfo(Patient patient)
        {
            Id = patient.Id;
            Username = patient.Username;
            FullName = patient.FullName;
            SNILS = patient.SNILS;
            DateOfReg = patient.DateOfReg;
        }
    }
}
