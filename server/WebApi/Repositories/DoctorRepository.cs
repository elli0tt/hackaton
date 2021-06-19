using System.Collections.Generic;
using System.Linq;
using WebApi.Data;
using WebApi.Models;

namespace WebApi.Repositories
{
    public class DoctorRepository
    {
        private readonly ApplicationContext _context;

        public DoctorRepository(ApplicationContext context)
        {
            _context = context;
        }

        public Doctor Create(Doctor d)
        {
            _context.Doctors.Add(d);
            _context.SaveChanges();
            return d;
        }

        public Doctor Insert(Doctor d) => Create(d);

        public Doctor Get(long id)
        {
            return _context.Doctors.Find(id);
        }

        public Doctor Get(string username)
        {
            return _context.Doctors.First(d => d.Username == username);
        }

        public List<Doctor> GetAll()
        {
            return _context.Doctors.ToList();
        }

        public bool IsIdExist(long id)
        {
            return _context.Doctors.Any(d => d.Id == id);
        }

        public bool IsUsernameExist(string username)
        {
            return _context.Doctors.Any(d => d.Username == username);
        }

        public Doctor Update(Doctor d)
        {
            var toUpdate = _context.Doctors.Find(d.Id);
            toUpdate = d;
            _context.Doctors.Update(toUpdate);
            _context.SaveChanges();
            return toUpdate;
        }

        public void Delete(long id)
        {
            var toDelete = _context.Doctors.Find(id);
            _context.Doctors.Remove(toDelete);
            _context.SaveChanges();
        }
    }
}
