using System.Collections.Generic;
using System.Linq;
using WebApi.Data;
using WebApi.Models;

namespace WebApi.Repositories
{
    public class PatientRepository
    {
        private readonly ApplicationContext _context;

        public PatientRepository(ApplicationContext context)
        {
            _context = context;
        }

        public Patient Create(Patient p)
        {
            _context.Patients.Add(p);
            _context.SaveChanges();
            return p;
        }

        public Patient Insert(Patient p) => Create(p);

        public Patient Get(string username)
        {
            return _context.Patients.First(p => p.Username == username);
        }

        public Patient GetById(long id)
        {
            return _context.Patients.Find(id);
        }

        public Patient GetBySnils(string snils)
        {
            return _context.Patients.First(p => p.SNILS == snils);
        }

        public List<Patient> GetAll()
        {
            return _context.Patients.ToList();
        }

        public bool IsExist(long id)
        {
            return _context.Patients.Any(p => p.Id == id);
        }

        public bool IsSnilsExist(string snils)
        {
            return _context.Patients.Any(p => p.SNILS == snils);
        }

        public bool IsUsernameExist(string username)
        {
            return _context.Patients.Any(p => p.Username == username);
        }

        public Patient Update(Patient p)
        {
            var toUpdate = _context.Patients.Find(p.Id);
            toUpdate.FullName = p.FullName;
            _context.Patients.Update(toUpdate);
            _context.SaveChanges();
            return toUpdate;
        }

        public void Delete(long id)
        {
            var toDelete = _context.Patients.Find(id);
            _context.Patients.Remove(toDelete);
            _context.SaveChanges();
        }
    }
}
