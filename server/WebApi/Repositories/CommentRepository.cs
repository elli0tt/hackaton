using System.Collections.Generic;
using System.Linq;
using WebApi.Data;
using WebApi.Models;

namespace WebApi.Repositories
{
    public class CommentRepository
    {
        private readonly ApplicationContext _context;

        public CommentRepository(ApplicationContext context)
        {
            _context = context;
        }

        public bool IsExist(long id)
        {
            return _context.Comments.Any(c => c.Id == id);
        }

        public DoctorComment Create(DoctorComment c)
        {
            _context.Comments.Add(c);
            _context.SaveChanges();
            return c;
        }

        public DoctorComment Insert(DoctorComment c) => Create(c);

        public DoctorComment Get(long id)
        {
            return _context.Comments.Find(id);
        }

        public List<DoctorComment> GetAll()
        {
            return _context.Comments.ToList();
        }

        public List<DoctorComment> GetAll(long doctorId)
        {
            return _context.Comments.Where(c => c.DoctorId == doctorId).ToList();
        }

        public List<DoctorComment> GetAll(string snils)
        {
            return _context.Comments.Where(c => c.PatientSNILS == snils).ToList();
        }

        public List<DoctorComment> GetAll(long doctorId, string snils)
        {
            return _context.Comments.Where(c => c.DoctorId == doctorId && c.PatientSNILS == snils).ToList();
        }

        public void Delete(long id)
        {
            var toDelete = _context.Comments.Find(id);
            _context.Comments.Remove(toDelete);
            _context.SaveChanges();
            return;
        }
    }
}
