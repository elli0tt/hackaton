using System;
using System.ComponentModel.DataAnnotations;
using WebApi.Enums;

namespace WebApi.Models
{
    public class Login
    {
        [Required]
        [Key]
        public long Id { get; set; }

        [Required]
        public Guid Token { get; set; }

        [Required]
        public long UserId { get; set; }

        [Required]
        public UserType UserType { get; set; }

        [Required]
        public DateTime Expires { get; set; }
    }
}
