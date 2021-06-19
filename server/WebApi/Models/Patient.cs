using System.ComponentModel.DataAnnotations;

namespace WebApi.Models
{
    public class Patient : UserModel
    {
        [Required]
        public string SNILS { get; set; }
    }
}
