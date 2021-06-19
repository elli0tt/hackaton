using System.ComponentModel.DataAnnotations;

namespace WebApi.Models.Support
{
    public class LogPass
    {
        [Required]
        public string Username { get; set; }

        [Required]
        public string Password { get; set; }

        public string SNILS { get; set; }
    }
}
