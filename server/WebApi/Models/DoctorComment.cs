using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace WebApi.Models
{
    public class DoctorComment
    {
        [Required]
        [Key]
        public long Id { get; set; }

        [Required]
        public string PatientSNILS { get; set; }

        [Required]
        public long DoctorId { get; set; }

        [Required]
        public string Comment { get; set; }

        [Required]
        public long DateTime { get; set; }

        public bool IsSent { get; set; } = false;
    }
}
