using System.ComponentModel.DataAnnotations;

namespace WebApi.Models.Support
{
    public class TonometerReadingFromUser
    {
        public long Id { get; set; }

        [Required]
        public long DateTime { get; set; }

        [Required]
        public int SYS { get; set; }

        [Required]
        public int DIA { get; set; }

        public int? Pulse { get; set; }

        [Required]
        public string Activity { get; set; }

        [Required]
        public string Comment { get; set; }
    }
}
