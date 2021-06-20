namespace WebApi.Tools
{
    public class Constants
    {
        /// <summary>
        /// Token lifetime
        /// in minutes
        /// </summary>
#if RELEASE
        public const int tokenLifetime = 3600 * 24 * 180;
#else
        public const int tokenLifetime = 60;
#endif

        /// <summary>
        /// Time in milliseconds when patient can delete or edit his tonometer reading
        /// </summary>
        public const int editOrDeleteReadingTime = 24 * 60 * 60 * 1000;

        public const string defaultExceptionText = "Exception";

        public const string tokenExpired = "Token has expired";

        public const string notPatient = "You are not a patient";
        public const string notDoctor = "You are not a doctor";
        
        public const string snilsNotExists = "No such SNILS";

        public const string bodyItemIncorrect = "Not enough fields";
    }
}
