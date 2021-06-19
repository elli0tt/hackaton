using Microsoft.AspNetCore.Cryptography.KeyDerivation;
using System.Security.Cryptography;

namespace WebApi.Tools
{
    public static class PswdCrypt
    {
        private static readonly RandomNumberGenerator rng = RandomNumberGenerator.Create();

        public static byte[] GenerateHash(string password, byte[] salt)
        {
            return KeyDerivation.Pbkdf2(
                password: password,
                salt: salt,
                prf: KeyDerivationPrf.HMACSHA1,
                iterationCount: 10000,
                numBytesRequested: 256 / 8);
        }

        public static byte[] GenerateSalt()
        {
            byte[] salt = new byte[128 / 8];
            rng.GetBytes(salt);
            return salt;
        }
    }
}
