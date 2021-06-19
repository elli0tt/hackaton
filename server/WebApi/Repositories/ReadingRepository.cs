using System.Collections.Generic;
using System.Linq;
using WebApi.Data;
using WebApi.Models;

namespace WebApi.Repositories
{
    public class ReadingRepository
    {
        private readonly ApplicationContext _context;

        public ReadingRepository(ApplicationContext context)
        {
            _context = context;
        }

        public bool IsExist(long id)
        {
            return _context.Readings.Any(r => r.Id == id);
        }

        public TonometerReading Create(TonometerReading r)
        {
            _context.Readings.Add(r);
            _context.SaveChanges();
            return r;
        }

        public TonometerReading Insert(TonometerReading r) => Create(r);

        public TonometerReading Get(long id)
        {
            return _context.Readings.Find(id);
        }

        public List<TonometerReading> GetAll()
        {
            return _context.Readings.ToList();
        }

        public List<TonometerReading> GetAll(string snils)
        {
            return _context.Readings.Where(r => r.PatientSNILS == snils).ToList();
        }

        public TonometerReading Update(long id, long dateTime = 0, int SYS = 0, int DIA = 0, int? pulse = null, string activity = null, string comment = null)
        {
            var toUpdate = _context.Readings.Find(id);
            if (dateTime != 0)
                toUpdate.DateTime = dateTime;
            if (SYS != 0)
                toUpdate.SYS = SYS;
            if (DIA != 0)
                toUpdate.DIA = DIA;
            if (pulse != null)
                toUpdate.Pulse = pulse;
            if (activity != null)
                toUpdate.Activity = activity;
            if (comment != null)
                toUpdate.Comment = comment;
            _context.Readings.Update(toUpdate);
            _context.SaveChanges();
            return toUpdate;
        }

        public TonometerReading Update(TonometerReading r)
        {
            var toUpdate = _context.Readings.Find(r.Id);
            toUpdate.Pulse = r.Pulse;
            toUpdate.SYS = r.SYS;
            toUpdate.DIA = r.DIA;
            toUpdate.DateTime = r.DateTime;
            toUpdate.Activity = r.Activity;
            toUpdate.Comment = r.Comment;
            _context.Readings.Update(toUpdate);
            _context.SaveChanges();
            return toUpdate;
        }

        public void Delete(long id)
        {
            var toDelete = _context.Readings.Find(id);
            _context.Readings.Remove(toDelete);
            _context.SaveChanges();
            return;
        }
    }
}
