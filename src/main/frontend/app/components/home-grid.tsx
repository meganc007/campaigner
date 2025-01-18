export default function HomeGrid() {
  return (
    <div className="flex flex-col p-4 md:p-6 lg:p-12">
      <p className="text-lg pb-4 w-fit text-center mx-auto">
        Forget mountains of notebooks. Campaigner is your ultimate campaign
        management tool, featuring:
      </p>
      <div className="grid grid-cols-2 text-center md:w-2/3 md:mx-auto">
        <div className="p-2 border-t-2 border-t-gunmetal border-r-2 border-r-gunmetal">
          <h3>Hierarchical Location Management</h3>
          <p>
            From continents and regions down to a singular dungeon, get a bird's
            eye view of all the locations in your world.
          </p>
        </div>
        <div className="p-2 border-t-2 border-t-gunmetal">
          <h3>Comprehensive Character Tracking</h3>
          <p>
            Manage all the people that bring your world to life. Track NPCs with
            ease: log their personalities, descriptions, locations, jobs, and
            even their personal inventories.
          </p>
        </div>
        <div className="p-2 border-t-2 border-t-gunmetal border-r-2 border-r-gunmetal">
          <h3>Custom Calendar Creation</h3>
          <p>
            Design unique calendars tailored to your world's lore and events.
          </p>
        </div>
        <div className="p-2  border-t-2 border-t-gunmetal">
          <h3>Event Timelines</h3>
          <p>
            Record historical milestones and plot events with an intuitive
            timeline system
          </p>
        </div>
        <div className="p-2 col-span-2 border-t-2 border-t-gunmetal">
          <h3>Quest Creation</h3>
          <p>
            Build dynamic quests with customizable hooks, objectives, and
            conditional outcomes. Whether your players are stealthy or prefer a
            more direct approach, our quest organization system has you covered.
          </p>
        </div>
      </div>
    </div>
  );
}
