namespace WebApi.Models.Support
{
    public class DoctorInfo
    {
        public long Id { get; set; }
        public string Username { get; set; }
        public string FullName { get; set; }
        public long DateOfReg { get; set; }

        public DoctorInfo(Doctor doctor, bool full = true)
        {
            if (full)
            {
                Id = doctor.Id;
                Username = doctor.Username;
            }
            FullName = doctor.FullName;
            DateOfReg = doctor.DateOfReg;
        }
    }
}
