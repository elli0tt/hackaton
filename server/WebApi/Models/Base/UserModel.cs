using System.ComponentModel.DataAnnotations;

namespace WebApi.Models
{
    public abstract class UserModel
    {
        [Required]
        [Key]
        public long Id { get; set; }

        [Required]
        public string Username { get; set; }

        [Required]
        public byte[] PasswordHash { get; set; }

        [Required]
        public byte[] PasswordSalt { get; set; }

        public string FullName { get; set; }

        [Required]
        public long DateOfReg { get; set; }
    }
}
