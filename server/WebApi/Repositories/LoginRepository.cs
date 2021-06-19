using System.Linq;
using WebApi.Data;
using WebApi.Enums;
using WebApi.Models;

namespace WebApi.Repositories
{
    public class LoginRepository
    {
        private readonly ApplicationContext _context;

        public LoginRepository(ApplicationContext context)
        {
            _context = context;
        }

        public Login Create(Login l)
        {
            _context.Logins.Add(l);
            _context.SaveChanges();
            return l;
        }

        public Login Insert(Login l) => Create(l);

        public Login Get(long id, UserType userType)
        {
            return _context.Logins.FirstOrDefault(l => l.Id == id && l.UserType == userType);
        }

        public Login Get(string token)
        {
            return _context.Logins.FirstOrDefault(l => l.Token.ToString() == token);
        }

        public bool IsExist(long id, UserType userType)
        {
            return _context.Logins.Any(l => l.Id == id && l.UserType == userType);
        }

        public bool IsTokenExist(string token)
        {
            return _context.Logins.Any(l => l.Token.ToString() == token);
        }

        public void Delete(long id, UserType userType)
        {
            var toDelete = Get(id, userType);
            _context.Logins.Remove(toDelete);
            _context.SaveChanges();
        }
    }
}
